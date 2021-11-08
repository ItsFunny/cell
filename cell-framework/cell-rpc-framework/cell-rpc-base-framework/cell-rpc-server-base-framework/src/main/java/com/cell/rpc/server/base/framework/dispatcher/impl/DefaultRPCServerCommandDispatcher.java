package com.cell.rpc.server.base.framework.dispatcher.impl;

import com.cell.base.common.context.InitCTX;
import com.cell.base.core.channel.IChannel;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.*;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.base.core.utils.ClassUtil;
import com.cell.rpc.common.cmd.IRPCServerCommand;
import com.cell.rpc.common.annotation.RPCServerReactorAnno;
import com.cell.rpc.common.dispatcher.abs.AbstractRPCCommandDispatcher;
import com.cell.rpc.server.base.framework.annotation.RPCDispatcherAnno;
import com.cell.rpc.server.base.framework.annotation.RPCServerCmdAnno;
import com.cell.rpc.server.base.framework.context.RPCServerCommandContext;
import com.cell.rpc.server.base.framework.couple.IRPCServerRequest;
import com.cell.rpc.server.base.framework.dispatcher.IRPCServerCommandDispatcher;
import com.cell.rpc.server.base.framework.reactor.IRPCServerReactor;
import com.cell.rpc.server.base.framework.suit.DefaultServerRPCCommandSuit;

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
        return commands.get(((IRPCServerRequest) request).getProtocol());
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
