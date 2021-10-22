package com.cell.dispatcher;

import com.cell.channel.IChannel;
import com.cell.constants.ContextConstants;
import com.cell.constants.ProtocolConstants;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.ICommandSuit;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.reactor.ICommandReactor;
import com.cell.wrapper.CommandWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 22:16
 */
public abstract class AbstractCommandDispatcher implements IDispatcher
{
    private Map<CommandProtocolID, CommandWrapper> commands = new HashMap<>();

    public AbstractCommandDispatcher(IChannel<IHandler, IChainHandler> channel)
    {
        this.channel = channel;
    }

    private IChannel<IHandler, IChainHandler> channel;

    @Override
    public void dispatch(IServerRequest request, IServerResponse response)
    {
        CommandProtocolID protocolId = request.getProtocolId();
        CommandWrapper wrapper = commands.get(protocolId);
        if (wrapper == null)
        {
            this.fastFail(response, getDefaultFailStatus());
            return;
        }
        ICommandSuit suit = this.createSuit(request, response, this.channel, wrapper);
        this.channel.readCommand(suit);
    }

    protected abstract ICommandSuit createSuit(IServerRequest request, IServerResponse response, IChannel<IHandler, IChainHandler> channel, CommandWrapper wrapper);


    protected abstract void onAddReactor(Map<CommandProtocolID, CommandWrapper> commands, ICommandReactor reactor);

    @Override
    public synchronized void addReactor(ICommandReactor reactor)
    {
        this.onAddReactor(this.commands, reactor);
    }

    // override
    protected long getDefaultFailStatus()
    {
        return ContextConstants.FAIL;
    }

    protected void fastFail(IServerResponse response, long status)
    {
        response.addHeader(ProtocolConstants.RESPONSE_HEADER_CODE, String.valueOf(ContextConstants.FAIL));
        response.addHeader(ProtocolConstants.RESPONSE_HEADER_MSG, "internal server error");
        response.setStatus(status);
    }

}
