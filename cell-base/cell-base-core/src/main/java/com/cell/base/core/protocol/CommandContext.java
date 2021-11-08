package com.cell.base.core.protocol;

import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.constants.NetworkConstants;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.channel.IChannel;
import com.cell.base.core.concurrent.base.Future;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:41
 */
@Data
public abstract class CommandContext
{
    public static final String TOKEN = "token";
    private IServerRequest request;
    private IServerResponse response;
    private String sessionKey;
    private Summary summary;
    private CommandWrapper wrapper;
    private IChannel<IHandler, IChainHandler> channel;

    protected abstract Summary collecSummary(IServerRequest request, CommandWrapper wrapper);

    public CommandContext(IChannel<IHandler, IChainHandler> channel, IServerRequest request, IServerResponse httpResponse,
                          CommandWrapper wrapper)
    {
        this.channel = channel;
        this.request = request;
        this.response = httpResponse;
        this.summary = collecSummary(request, wrapper);
        this.wrapper = wrapper;
        sessionKey = getHeaderData(NetworkConstants.SESSION_KEY);
        httpResponse.getPromise().addListener(this::onComplete);
    }

    protected abstract void onComplete(Future<? super Object> future) throws Exception;

    protected String getHeaderData(String headerName)
    {
        return this.request.getHeader(headerName);
    }

    protected String getHeaderData(String headerName, String defaultValue)
    {
        String ret = this.request.getHeader(headerName);
        ret = StringUtils.isEmpty(ret) ? defaultValue : ret;
        return ret;
    }
}
