package com.cell.dispatcher.impl;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.channel.IRPCChannel;
import com.cell.cmd.IRPCServerCommand;
import com.cell.constants.ContextConstants;
import com.cell.constants.ProtocolConstants;
import com.cell.context.RPCServerCommandContext;
import com.cell.dispatcher.IRPCServerCommandDispatcher;
import com.cell.dispatcher.RPCServerCommandWrapper;
import com.cell.exceptions.ProgramaException;
import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.DefaultStringCommandProtocolID;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.reacotr.IRPCReactor;
import com.cell.reactor.IRPCServerReactor;
import com.cell.suit.DefaultServerRPCCommandSuit;
import com.cell.utils.ClassUtil;
import com.cell.utils.StringUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;

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
@Data
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
    public void dispatch(IServerRequest request, IServerResponse response)
    {
        CommandProtocolID protocolId = request.getProtocolId();
        RPCServerCommandWrapper rpcCommandWrapper = commands.get(protocolId);
        if (rpcCommandWrapper == null)
        {
            this.fastFail(response);
            return;
        }
        IRPCServerReactor reactor = rpcCommandWrapper.getReactor();
        Class<? extends IRPCServerCommand> cmd = rpcCommandWrapper.getCmd();
        RPCServerCommandContext rpcServerCommandContext = new RPCServerCommandContext();
        rpcCommandWrapper.setReactor(reactor);
        rpcCommandWrapper.setCmd(cmd);
        DefaultServerRPCCommandSuit suit = new DefaultServerRPCCommandSuit(this.rpcChannel, rpcServerCommandContext);
        this.rpcChannel.readCommand(suit);
    }

    private void fastFail(IServerResponse response)
    {
        response.addHeader(ProtocolConstants.RESPONSE_HEADER_CODE, String.valueOf(ContextConstants.FAIL));
        response.addHeader(ProtocolConstants.RESPONSE_HEADER_MSG, "internal server error");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @Override
    public synchronized void addReactor(IRPCReactor reactor)
    {
        IRPCServerReactor serverReactor = (IRPCServerReactor) reactor;
        RPCServerReactorAnno anno = ClassUtil.getMergedAnnotation(serverReactor.getClass(), RPCServerReactorAnno.class);
        Class<? extends IRPCServerCommand>[] serverCommmands = anno.cmds();
        Stream.of(serverCommmands).filter(p -> ClassUtil.getMergedAnnotation(p, RPCServerCmdAnno.class) != null).forEach(serverCommmand ->
        {
            String group = anno.group();
            group = StringUtils.isEmpty(group) ? "/default" : group;
            RPCServerCmdAnno rpcServerCmdAnno = ClassUtil.getMergedAnnotation(serverCommmand, RPCServerCmdAnno.class);
            float version = rpcServerCmdAnno.version();
            String func = rpcServerCmdAnno.func();
            if (StringUtils.isEmpty(func))
            {
                throw new ProgramaException("funcs 不可为空");
            }
            String protocolId = String.join("/", group, func, version + "");
            protocolId = protocolId.replaceAll("//", "/");
            DefaultStringCommandProtocolID stringCommandProtocolID = new DefaultStringCommandProtocolID(protocolId);
            RPCServerCommandWrapper wrapper = new RPCServerCommandWrapper();
            wrapper.setCmd(serverCommmand);
            wrapper.setReactor(serverReactor);
            this.commands.put(stringCommandProtocolID, wrapper);
        });
    }
}
