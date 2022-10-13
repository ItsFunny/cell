package com.cell.extension.task.worker;

import com.cell.base.common.events.IEvent;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.extension.task.wrapper.PromiseWrapper;


public interface PromiseWorker extends JobWorker
{
    @Override
    default void doExecuteJob(IEvent event)
    {
        PromiseWrapper wp = (PromiseWrapper) event;
        if (wp.isHandled())
        {
            return;
        }
        Promise promise = wp.getPromise();
        wp.handle();
        try
        {
            Object ret = this.doJob(wp.getData());
            promise.trySuccess(ret);
        } catch (Exception e)
        {
            promise.tryFailure(e);
        }
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
