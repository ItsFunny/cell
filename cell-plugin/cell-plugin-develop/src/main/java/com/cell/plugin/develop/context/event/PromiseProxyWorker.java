package com.cell.plugin.develop.context.event;

import com.cell.base.common.events.IEvent;
import io.netty.util.concurrent.Promise;

public class PromiseProxyWorker implements JobWorker
{
    private JobWorker internal;

    public PromiseProxyWorker(JobWorker internal)
    {
        this.internal = internal;
    }

    @Override
    public Object doExecuteJob(IEvent event)
    {
        PromiseWrapper wp = (PromiseWrapper) event;
        if (wp.isHandled())
        {
            return null;
        }
        Promise promise = wp.getPromise();
        wp.handle();
        try
        {
            Object ret = this.internal.doExecuteJob(wp.getData());
            promise.trySuccess(ret);
            return ret;
        } catch (Exception e)
        {
            promise.tryFailure(e);
        }
        return null;
    }

    @Override
    public boolean predict(IEvent event)
    {
        if (!(event instanceof PromiseWrapper))
        {
            return false;
        }
        return this.internal.predict(((PromiseWrapper<?>) event).getData());
    }
}
