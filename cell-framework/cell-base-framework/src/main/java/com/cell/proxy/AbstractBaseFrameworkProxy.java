package com.cell.proxy;

import com.cell.concurrent.base.Promise;
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
        IFrameworkEvent fe = (IFrameworkEvent) event;
        IServerRequest request = fe.getRequest();
        IServerResponse response = fe.getResponse();
        response.setPromise(promise);
        this.dispatcher.dispatch(request, response);
    }

    protected abstract void afterProxy(IServerRequest request, IServerResponse response, Promise<Object> promise);
}
