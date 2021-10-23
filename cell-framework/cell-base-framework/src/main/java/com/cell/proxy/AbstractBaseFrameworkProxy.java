package com.cell.proxy;

import com.cell.concurrent.base.Promise;
import com.cell.context.DispatchContext;
import com.cell.dispatcher.IDispatcher;
import com.cell.event.IFrameworkEvent;
import com.cell.event.IProcessEvent;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 18:57
 */
public abstract class AbstractBaseFrameworkProxy extends AbstractProxy implements IFrameworkProxy
{
    private IDispatcher dispatcher;

    public AbstractBaseFrameworkProxy(IDispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }

    @Override
    public void onProxy(IProcessEvent event, Promise<Object> promise)
    {
        this.proxy(event);
    }

    @Override
    public void proxy(IProcessEvent event)
    {
        IFrameworkEvent fe = (IFrameworkEvent) event;
        IServerRequest request = fe.getRequest();
        IServerResponse response = fe.getResponse();
        DispatchContext context = new DispatchContext();
        context.setServerRequest(request);
        context.setServerResponse(response);
        this.dispatcher.dispatch(context);
    }

    @Override
    public IDispatcher getDispatcher()
    {
        return this.dispatcher;
    }

    protected abstract void afterProxy(IServerRequest request, IServerResponse response, Promise<Object> promise);
}
