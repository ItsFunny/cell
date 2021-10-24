package com.cell.protocol;

import com.cell.channel.IChannel;
import com.cell.concurrent.base.Future;
import com.cell.constants.NetworkConstants;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.utils.StringUtils;
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
        this.channel=channel;
        this.request = request;
        this.response = httpResponse;
        this.summary = collecSummary(request, wrapper);
        this.wrapper=wrapper;
        sessionKey = getHeaderData(NetworkConstants.SESSION_KEY);
        httpResponse.getPromise().addListener(this::onComplete);
    }

    protected abstract void onComplete(Future<? super Object> future) throws Exception;

    private String getHeaderData(String headerName)
    {
        return this.request.getHeader(headerName);
    }

    private String getHeaderData(String headerName, String defaultValue)
    {
        String ret = this.request.getHeader(headerName);
        ret = StringUtils.isEmpty(ret) ? defaultValue : ret;
        return ret;
    }
}
