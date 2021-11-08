package com.cell.base.framework.dispatcher;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.core.annotations.Command;
import com.cell.base.core.channel.IChannel;
import com.cell.base.core.concurrent.DummyExecutor;
import com.cell.base.core.concurrent.base.EventExecutor;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.concurrent.promise.BaseDefaultPromise;
import com.cell.base.core.constants.ContextConstants;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.CommandWrapper;
import com.cell.base.core.protocol.ICommandSuit;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.base.framework.context.DispatchContext;

import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 22:16
 */
public abstract class AbstractCommandDispatcher extends AbstractInitOnce implements IDispatcher
{
    private Map<String, CommandWrapper> commands = new HashMap<>();
    private IChannel<IHandler, IChainHandler> channel;

    private EventLoopGroup eventLoopGroup;


    @Override
    public void dispatch(DispatchContext context)
    {
        IServerRequest request = context.getServerRequest();
        IServerResponse response = context.getServerResponse();
        CommandWrapper wrapper = this.getCommandFromRequest(this.commands, request);
        if (wrapper == null)
        {
            this.fastFail(response, getDefaultFailStatus());
            return;
        }
        Command commandAnno = wrapper.getCommandAnno();
        EventExecutor eventExecutor = commandAnno.async() ? this.eventLoopGroup.next() : DummyExecutor.getInstance();
        Promise<Object> promise = new BaseDefaultPromise(eventExecutor);
        response.setPromise(promise);
        if (null != context.getOnOperationComplete())
        {
            promise.addListener(context.getOnOperationComplete());
        }
        ICommandSuit suit = this.createSuit(request, response, this.channel, wrapper);
        suit.setCommandEventExecutor(eventExecutor);
        this.channel.readCommand(suit);
    }

    protected abstract CommandWrapper getCommandFromRequest(Map<String, CommandWrapper> commands, IServerRequest request);


    protected abstract ICommandSuit createSuit(IServerRequest request, IServerResponse response, IChannel<IHandler, IChainHandler> channel, CommandWrapper wrapper);


    protected abstract void onAddReactor(Map<String, CommandWrapper> commands, ICommandReactor reactor);

    @Override
    public synchronized void addReactor(ICommandReactor reactor)
    {
        this.onAddReactor(this.commands, reactor);
    }

    @Override
    public void setChannel(IChannel<IHandler, IChainHandler> channel)
    {
        this.channel = channel;
    }

    @Override
    public List<? extends ICommandReactor> getReactors()
    {
        Set<ICommandReactor> reactors = new HashSet<>();
        Set<String> ids = this.commands.keySet();
        for (String commandProtocolID : ids)
        {
            ICommandReactor reactor = this.commands.get(commandProtocolID).getReactor();
            reactors.add(reactor);
        }
        return new ArrayList<>(reactors);
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
        response.fireFailure(new ProgramaException("no such protocol"));
    }

    @Override
    public void setEventGroup(EventLoopGroup eventLoopGroup)
    {
        this.eventLoopGroup = eventLoopGroup;
    }
}
