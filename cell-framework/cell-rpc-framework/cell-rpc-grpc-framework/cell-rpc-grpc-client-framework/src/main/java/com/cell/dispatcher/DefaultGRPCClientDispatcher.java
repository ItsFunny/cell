package com.cell.dispatcher;

import com.cell.channel.IChannel;
import com.cell.context.InitCTX;
import com.cell.dispatcher.impl.DefaultRPCClientCommandDispatcher;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.CommandWrapper;
import com.cell.protocol.ICommandSuit;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.reactor.ICommandReactor;

import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:19
 */
public class DefaultGRPCClientDispatcher extends DefaultRPCClientCommandDispatcher
{

    @Override
    protected CommandWrapper getCommandFromRequest(Map<String, CommandWrapper> commands, IServerRequest request)
    {
        return null;
    }

    @Override
    protected ICommandSuit createSuit(IServerRequest request, IServerResponse response, IChannel<IHandler, IChainHandler> channel, CommandWrapper wrapper)
    {
        return null;
    }

    @Override
    protected void onAddReactor(Map<String, CommandWrapper> commands, ICommandReactor reactor)
    {

    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
