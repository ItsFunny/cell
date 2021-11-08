package com.cell.dispatcher;

import com.cell.base.core.channel.IChannel;
import com.cell.base.common.context.InitCTX;
import com.cell.dispatcher.impl.DefaultRPCClientCommandDispatcher;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.CommandWrapper;
import com.cell.base.core.protocol.ICommandSuit;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.base.core.reactor.ICommandReactor;

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
