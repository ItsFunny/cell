package com.cell.protocol;

import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Promise;
import com.cell.constants.ContextConstants;
import com.cell.reactor.ICommandReactor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:33
 */
@Data
public abstract class AbstractBaseContext implements IBuzzContext
{
    protected String sequenceId;
    protected long requestTimestamp;

    private Summary summary;
    protected ICommandReactor reactor;
    private CommandContext context;
    private EventExecutor eventExecutor;

    private String ip;


    public AbstractBaseContext(CommandContext commandContext)
    {
        this.requestTimestamp = System.currentTimeMillis();
        this.context = commandContext;
//        promise.addListener((BaseFutureListener) future ->
//        {
//            if (!future.isSuccess())
//            {
//                LOG.error(Module.HTTP_FRAMEWORK, future.cause(), "sequenceId = {}, send response  fail", getSequenceId());
//                discard();
//            }
//        });
    }

    @Override
    public CommandContext getCommandContext()
    {
        return this.context;
    }

    protected boolean timeout(long status)
    {
        return (status & ContextConstants.TIMEOUT) >= ContextConstants.TIMEOUT;
    }

    protected boolean programError(long status)
    {
        return (status & ContextConstants.PROGRAMA_ERROR) >= ContextConstants.PROGRAMA_ERROR;
    }

    @Override
    public void response(ContextResponseWrapper wp)
    {

    }

    @Override
    public Promise<Object> getPromise()
    {
        return this.context.getResponse().getPromise();
    }

    @Override
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    @Override
    public String getIp()
    {
        return this.ip;
    }
}
