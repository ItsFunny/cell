package com.cell.context.impl;

import com.cell.concurrent.base.Promise;
import com.cell.constants.ContextConstants;
import com.cell.constants.ProtocolConstants;
import com.cell.context.IRPCServerCommandContext;
import com.cell.context.RPCServerCommandContext;
import com.cell.exception.RPCFrameworkException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.*;

import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:40
 */
public abstract class AbstractRPCServerCommandContext extends AbstractBaseContext implements IRPCServerCommandContext
{
    private RPCServerCommandContext commandContext;

    public AbstractRPCServerCommandContext(RPCServerCommandContext commandContext)
    {
        super();
        this.commandContext = commandContext;
    }



    @Override
    public IServerRequest getRequest()
    {
        return this.commandContext.getRequest();
    }

    @Override
    public CommandProtocolID getProtocolID()
    {
        return this.commandContext.getProtocolID();
    }

    @Override
    public Promise<Object> getPromise()
    {
        return this.commandContext.getResponse().getPromise();
    }

    @Override
    public void response(ContextResponseWrapper wp)
    {
        long currentTime = System.currentTimeMillis();
        long consumeTime = currentTime - this.getRequestTimestamp();
        final String sequenceId = this.commandContext.getSummary().getSequenceId();
        LOG.info(Module.RPC_FRAMEWORK, "response,protocol={},ip={},sequenceId={},cost={}",
                this.commandContext.getProtocolID(), this.getIp(), sequenceId, consumeTime);


        IServerResponse response = this.commandContext.getResponse();
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
            this.commandContext.getResponse().fireResult(wp.getRet());
            this.getPromise().trySuccess(null);
            return;
        }

        long status = wp.getStatus();
        if (this.timeout(status))
        {
            LOG.warn(Module.HTTP_FRAMEWORK, "触发了超时,cost={},info={}", consumeTime, wp);
        }
        if (this.commandContext.getResponse().isSetOrExpired())
        {
            LOG.error("sequenceId = {}, duplicated response for request {} ", sequenceId, wp.getCmd());
            // FIXME
            this.getPromise().setFailure(new RPCFrameworkException("duplicate result", ""));
            return;
        }

        this.commandContext.getResponse().setStatus(status);
        this.commandContext.getResponse().fireResult(wp.getRet());
        this.getPromise().trySuccess(null);
    }


    @Override
    public void discard()
    {
        this.response(ContextResponseWrapper.builder()
                .status(ContextConstants.FAIL)
                .ret("BAD REQUEST")
                .msg("BAD REQUEST")
                .build());
    }
}
