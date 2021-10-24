package com.cell.context;

import com.cell.channel.IChannel;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.*;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:51
 */
@Data
public class RPCServerCommandContext extends CommandContext
{
    private CommandProtocolID protocolID;

    public RPCServerCommandContext(IChannel<IHandler, IChainHandler> channel, IServerRequest request, IServerResponse httpResponse, CommandWrapper wrapper)
    {
        super(channel, request, httpResponse, wrapper);
    }

    @Override
    protected Summary collecSummary(IServerRequest request, CommandWrapper wrapper)
    {
        return null;
    }

    @Override
    protected void onComplete(com.cell.concurrent.base.Future<? super Object> future) throws Exception
    {

    }


}
