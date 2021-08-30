package com.cell.context;

import com.cell.command.IHttpCommand;
import com.cell.exception.HttpFramkeworkException;
import com.cell.hook.IHttpCommandHook;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandContext;
import com.cell.util.HttpUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:34
 */
@Data
public abstract class AbstractHttpCommandContext extends AbstractBaseContext implements IHttpContext
{
    protected CommandContext commandContext;
    // FIXME ,有更好的做法
    protected IHttpCommandHook hook;

    protected IHttpCommand cmd;

    public AbstractHttpCommandContext(CommandContext commandContext, IHttpCommandHook hk)
    {
        this.commandContext = commandContext;
        this.hook = hk;
    }

    @Override
    public void response(HttpStatus status, Object obj)
    {
        long currentTime = System.currentTimeMillis();
        long consumeTime = currentTime - this.commandContext.getRequestTimestamp();
        final String sequenceId = this.commandContext.getSummary().getSequenceId();

        if (consumeTime > this.commandContext.getSummary().getTimeOut())
        {
            LOG.warn(Module.HTTP_FRAMEWORK, "超时");
            // FIXME ,需要获取得到cmd
            this.hook.exceptionCaught(new HttpFramkeworkException(cmd, "", "asd"));
            return;
        }

    }

    @Override
    public void discard() throws IOException
    {
        this.commandContext.discard();
    }

//    @Override
//    public void autoResponse()
//    {
//        long now = System.currentTimeMillis();
//        long requestTimestamp = this.commandContext.getRequestTimestamp();
//        long consumeTime = now - requestTimestamp;
//        final String sequenceId = HttpUtils.get(httpCommand);
//    }

    @Override
    public void success(Object ret)
    {
        this.response(HttpStatus.OK, ret);
    }

    @Override
    public String getURI()
    {
        return this.commandContext.getURI();
    }

    @Override
    public void fail(Object obj)
    {
        this.response(HttpStatus.BAD_REQUEST, obj);
    }
}
