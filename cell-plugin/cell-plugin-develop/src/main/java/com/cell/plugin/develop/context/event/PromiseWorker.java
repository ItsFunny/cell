package com.cell.plugin.develop.context.event;

import com.cell.base.common.events.IEvent;
import io.netty.util.concurrent.Promise;


public interface PromiseWorker extends JobWorker
{
    @Override
    default Object doExecuteJob(IEvent event)
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
            Object ret = this.doJob(wp.getData());
            promise.trySuccess(ret);
            return ret;
        } catch (Exception e)
        {
            promise.tryFailure(e);
        }
        return null;
    }

    @Override
    default boolean predict(IEvent event)
    {
        if (!(event instanceof PromiseWrapper))
        {
            return false;
        }
        return this.doPredict(((PromiseWrapper) event).getData());
    }

    boolean doPredict(IEvent event);

    Object doJob(IEvent event);

}
