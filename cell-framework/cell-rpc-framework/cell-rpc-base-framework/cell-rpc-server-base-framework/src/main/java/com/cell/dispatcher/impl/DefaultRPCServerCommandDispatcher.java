package com.cell.dispatcher.impl;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.channel.IRPCChannel;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.IRPCContext;
import com.cell.context.RPCServerCommandContext;
import com.cell.dispatcher.IRPCServerCommandDispatcher;
import com.cell.dispatcher.RPCServerCommandWrapper;
import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.DefaultStringCommandProtocolID;
import com.cell.protocol.ICommand;
import com.cell.reacotr.IRPCReactor;
import com.cell.reactor.IRPCServerReactor;
import com.cell.suit.DefaultServerRPCCommandSuit;
import com.cell.utils.ClassUtil;
import com.cell.utils.StringUtils;

import java.util.HashMap;
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
public class DefaultRPCServerCommandDispatcher implements IRPCServerCommandDispatcher
{
    private static final DefaultRPCServerCommandDispatcher instance = new DefaultRPCServerCommandDispatcher();

    public static DefaultRPCServerCommandDispatcher getInstance()
    {
        return instance;
    }

    private Map<CommandProtocolID, RPCServerCommandWrapper> commands = new HashMap<>();

    private IRPCChannel rpcChannel;

    @Override
    public void dispatch(IRPCContext context)
    {
        CommandProtocolID protocolID = context.getProtocolID();
        RPCServerCommandWrapper rpcCommandWrapper = commands.get(protocolID);
        if (rpcCommandWrapper == null)
        {
            context.discard();
            return;
        }
        IRPCServerReactor reactor = rpcCommandWrapper.getReactor();
        Class<? extends IRPCServerCommand> cmd = rpcCommandWrapper.getCmd();
        RPCServerCommandContext rpcServerCommandContext=new RPCServerCommandContext();
        rpcCommandWrapper.setReactor(reactor);
        rpcCommandWrapper.setCmd(cmd);
        DefaultServerRPCCommandSuit suit = new DefaultServerRPCCommandSuit(this.rpcChannel, rpcServerCommandContext);
        this.rpcChannel.readCommand(suit);
    }

    @Override
    public synchronized void addReactor(IRPCReactor reactor)
    {
        IRPCServerReactor serverReactor = (IRPCServerReactor) reactor;
        ReactorAnno anno = ClassUtil.getMergedAnnotation(serverReactor.getClass(), ReactorAnno.class);
        Class<? extends ICommand>[] serverCommmands = anno.cmds();
        Stream.of(serverCommmands).filter(p -> ClassUtil.getMergedAnnotation(p, RPCServerCmdAnno.class) != null).forEach(serverCommmand ->
        {
            String group = anno.group();
            group = StringUtils.isEmpty(group) ? "/default" : group;
            RPCServerCmdAnno rpcServerCmdAnno = ClassUtil.getMergedAnnotation(serverCommmand, RPCServerCmdAnno.class);
            float version = rpcServerCmdAnno.version();
            String func = rpcServerCmdAnno.func();
            String protocolId = String.join("/", group, func, version + "");
            DefaultStringCommandProtocolID stringCommandProtocolID = new DefaultStringCommandProtocolID(protocolId);
            RPCServerCommandWrapper wrapper = new RPCServerCommandWrapper();
            wrapper.setCmd((Class<? extends IRPCServerCommand>) serverCommmand);
            wrapper.setReactor(serverReactor);
            this.commands.put(stringCommandProtocolID, wrapper);
        });
    }
}
