package com.cell.protocol;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Promise;
import com.cell.constants.ContextConstants;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.ICommandReactor;
import lombok.Data;

import java.util.Map;

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
    protected long requestTimestamp;

    private CommandContext context;
    private EventExecutor eventExecutor;


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
    public Summary getSummary()
    {
        return this.context.getSummary();
    }

    @Override
    public ICommandReactor getReactor()
    {
        return this.context.getWrapper().getReactor();
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
        long currentTime = System.currentTimeMillis();
        long consumeTime = currentTime - this.getRequestTimestamp();
        final String sequenceId = this.getCommandContext().getSummary().getSequenceId();
        LOG.info(Module.ALL,
                "response protocol={}, ip={},sequenceId={},cost={}",
                this.getCommandContext().getSummary().getProtocolId(), this.getSummary().getRequestIP(), sequenceId, consumeTime);

        IServerResponse response = this.getCommandContext().getResponse();
        if (wp.getHeaders() != null)
        {
            Map<String, String> headers = wp.getHeaders();
            for (String s : headers.keySet())
            {
                response.addHeader(s, headers.get(s));
            }
        }

        response.addHeader(ProtocolConstants.RESPONSE_HEADER_CODE, String.valueOf(wp.getStatus()));
        response.addHeader(ProtocolConstants.RESPONSE_HEADER_MSG, wp.getMsg());


        // 提前结束
        if (null != wp.getException())
        {
            LOG.error(Module.HTTP_FRAMEWORK, wp.getException(), "调用失败,from:{}", wp.getFrom());
            response.fireResult(null);
            return;
        }

        if (null != wp.getOther())
        {
            this.decorateOnHandleOther(response, wp.getOther());
        }

        // 提前结束
        if (null != wp.getException())
        {
            LOG.error(Module.ALL, wp.getException(), "调用失败,from:{}", wp.getFrom());
            response.fireResult(null);
            return;
        }

        long status = wp.getStatus();
        if (this.timeout(status))
        {
            LOG.warn(Module.HTTP_FRAMEWORK, "触发了超时,cost={},info={}", consumeTime, wp);
        }

        if (this.isSetOrExpired())
        {
            LOG.error("sequenceId = {}, duplicated response for request {} ", sequenceId, wp.getCmd());
            // FIXME
            response.fireFailure(new ProgramaException("duplicate result"));
            return;
        }

        // success
        if (this.success(status))
        {
            this.decorateOnSuccess(response);
        } else
        {
            this.decorateOnFail(response);
        }
        Object ret = wp.getRet();
        try
        {
            ret = this.decorateRetBeforeFire(ret);
        } finally
        {
            response.fireResult(ret);
        }
    }

    protected void decorateOnSuccess(IServerResponse response)
    {
        response.setStatus(ContextConstants.SUCCESS);
    }

    protected void decorateOnFail(IServerResponse response)
    {
        response.setStatus(ContextConstants.FAIL);
    }

    protected Object decorateRetBeforeFire(Object ret)
    {
        return ret;
    }

    protected boolean success(long status)
    {
        return (status & ContextConstants.SUCCESS) >= ContextConstants.SUCCESS;
    }

    protected void decorateOnHandleOther(IServerResponse response, Object o)
    {

    }

    protected abstract boolean isSetOrExpired();

    @Override
    public Promise<Object> getPromise()
    {
        return this.context.getResponse().getPromise();
    }
}
