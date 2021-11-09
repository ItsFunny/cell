package com.cell.http.framework.protocol;

import com.cell.base.common.constants.DebugConstants;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.common.utils.UUIDUtils;
import com.cell.base.core.channel.IChannel;
import com.cell.base.core.concurrent.base.Future;
import com.cell.base.core.constants.NetworkConstants;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.*;
import com.cell.http.framework.couple.IHttpServerRequest;
import com.cell.http.framework.couple.IHttpServerResponse;
import com.cell.http.framework.util.HttpUtils;
import lombok.Data;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:41
 */
@Data
public class HttpCommandContext extends CommandContext
{
    public static final String TOKEN = "token";
    // 返回给 http上层的结果值
    private DeferredResult<Object> responseResult;

    public HttpCommandContext(IChannel<IHandler, IChainHandler> channel,
                              IServerRequest request,
                              IServerResponse httpResponse,
                              CommandWrapper wrapper)
    {
        super(channel, request, httpResponse, wrapper);
        String timeout = getHeaderData(NetworkConstants.TIME_OUT);
        long defaultTimeount = wrapper.getCommandAnno().timeOut();
        if (!StringUtils.isEmpty(timeout))
        {
            try
            {
                defaultTimeount = Long.parseLong(timeout);
            } catch (Exception e)
            {
            }
        }
        DeferredResult<Object> responseResult = new DeferredResult<>(defaultTimeount);
        this.responseResult = responseResult;
        ((IHttpServerResponse) this.getResponse()).setDeferredResponse(responseResult);
    }

    @Override
    protected void onComplete(Future<? super Object> future) throws Exception
    {
        if (future.isSuccess())
        {
            this.responseResult.setResult(future.get());
        } else
        {
            Throwable cause = future.cause();
            this.responseResult.setResult(this.getWrapper().getFallBackMethod().invoke(this.getRequest(), this.getResponse(), cause));
        }
    }

    @Override
    protected Summary collecSummary(IServerRequest req, CommandWrapper wrapper)
    {
        IHttpServerRequest request = (IHttpServerRequest) req;
        HttpSummary httpSummary = new HttpSummary();
        httpSummary.setRequestIP(HttpUtils.getIpAddress(request.getInternalRequest()));
        httpSummary.setProtocolId(request.getInternalRequest().getRequestURL().toString());
        httpSummary.setToken(getHeaderData(TOKEN));
        httpSummary.setReceiveTimestamp(System.currentTimeMillis());
        httpSummary.setSequenceId(getHeaderData(DebugConstants.SEQUENCE_ID, UUIDUtils.uuid2()));
        return httpSummary;
    }


}
