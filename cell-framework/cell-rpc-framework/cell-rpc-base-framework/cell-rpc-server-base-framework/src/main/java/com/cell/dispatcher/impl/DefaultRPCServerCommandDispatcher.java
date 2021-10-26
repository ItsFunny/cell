package com.cell.dispatcher.impl;

import com.cell.annotation.RPCDispatcherAnno;
import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.channel.IChannel;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.InitCTX;
import com.cell.context.RPCServerCommandContext;
import com.cell.dispatcher.IRPCServerCommandDispatcher;
import com.cell.dispatcher.abs.AbstractRPCCommandDispatcher;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.*;
import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IRPCServerReactor;
import com.cell.suit.DefaultServerRPCCommandSuit;
import com.cell.utils.ClassUtil;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 16:59
 */
@RPCDispatcherAnno
public class DefaultRPCServerCommandDispatcher extends AbstractRPCCommandDispatcher implements IRPCServerCommandDispatcher
{
    public DefaultRPCServerCommandDispatcher()
    {
    }


    @Override
    protected CommandWrapper getCommandFromRequest(Map<String, CommandWrapper> commands, IServerRequest request)
    {
        return null;
    }

    @Override
    protected ICommandSuit createSuit(IServerRequest request, IServerResponse response, IChannel<IHandler, IChainHandler> channel, CommandWrapper wrapper)
    {
        RPCServerCommandContext rpcServerCommandContext = new RPCServerCommandContext(channel, request, response, wrapper);
        Class<? extends IRPCServerCommand> rpcServerCmd = (Class<? extends IRPCServerCommand>) wrapper.getCmd();
        DefaultServerRPCCommandSuit suit = new DefaultServerRPCCommandSuit(rpcServerCommandContext, (IRPCServerReactor) wrapper.getReactor(), rpcServerCmd);
        return suit;
    }

    @Override
    protected void onAddReactor(Map<String, CommandWrapper> commands, ICommandReactor reactor)
    {
        IRPCServerReactor serverReactor = (IRPCServerReactor) reactor;
        RPCServerReactorAnno anno = ClassUtil.getMergedAnnotation(serverReactor.getClass(), RPCServerReactorAnno.class);
        Class<? extends IRPCServerCommand>[] serverCommmands = anno.cmds();
        Stream.of(serverCommmands).filter(p -> ClassUtil.getMergedAnnotation(p, RPCServerCmdAnno.class) != null).forEach(serverCommmand ->
        {
            RPCServerCmdAnno rpcServerCmdAnno = ClassUtil.getMergedAnnotation(serverCommmand, RPCServerCmdAnno.class);
            String protocolId = rpcServerCmdAnno.protocol();
            DefaultStringCommandProtocolID stringCommandProtocolID = new DefaultStringCommandProtocolID(protocolId);
            CommandWrapper wrapper = new CommandWrapper();
            wrapper.setCmd(serverCommmand);
            wrapper.setReactor(serverReactor);
            commands.put(stringCommandProtocolID.id(), wrapper);
        });
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
