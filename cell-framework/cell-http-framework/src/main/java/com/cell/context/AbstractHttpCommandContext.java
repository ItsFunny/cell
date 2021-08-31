package com.cell.context;

import com.cell.command.IHttpCommand;
import com.cell.exception.HttpFramkeworkException;
import com.cell.hook.IHttpCommandHook;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandContext;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.reactor.IHttpReactor;
import com.cell.util.HttpUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
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

    protected IHttpReactor reactor;

    protected IHttpCommand cmd;

    public AbstractHttpCommandContext(CommandContext commandContext, IHttpCommandHook hk)
    {
        this.commandContext = commandContext;
        this.hook = hk;
    }

    @Override
    public DeferredResult<Object> getResult()
    {
        return this.commandContext.getResponseResult();
    }

    @Override
    public void response(ContextResponseWrapper wp)
    {
        long currentTime = System.currentTimeMillis();
        long consumeTime = currentTime - this.getRequestTimestamp();
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
    public String getURI()
    {
        return this.commandContext.getURI();
    }

    @Override
    public HttpServletRequest getHttpRequest()
    {
        return this.commandContext.getHttpRequest();
    }
}
