package com.cell.dispatcher.impl;

import com.cell.channel.IChannel;
import com.cell.context.InitCTX;
import com.cell.dispatcher.IRPCClientCommandDispatcher;
import com.cell.dispatcher.abs.AbstractRPCCommandDispatcher;
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
 * @Date 创建时间：2021-10-27 04:46
 */
public class DefaultRPCClientCommandDispatcher extends AbstractRPCCommandDispatcher implements IRPCClientCommandDispatcher
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
