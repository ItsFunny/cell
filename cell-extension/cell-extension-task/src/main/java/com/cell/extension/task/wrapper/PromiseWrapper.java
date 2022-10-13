package com.cell.extension.task.wrapper;

import com.cell.base.common.events.IEvent;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.concurrent.promise.BaseDefaultPromise;
import com.cell.extension.task.event.JobCenter;


public class PromiseWrapper<T> implements IEvent
{
    private Promise<Object> promise;
    private boolean handled;

    private IEvent data;

    public PromiseWrapper(IEvent data)
    {
        this.data = data;
        this.promise = new BaseDefaultPromise(JobCenter.getInstance().getEventExecutors().next());
    }

    public IEvent getData()
    {
        return data;
    }

    public void handle()
    {
        this.handled = true;
    }

    public boolean isHandled()
    {
        return this.handled;
    }

    public void done(T t)
    {
        this.promise.trySuccess(t);
    }

    public Object waitUntil()
    {
        try
        {
            return this.promise.get();
        } catch (Exception e)
        {
            throw new RuntimeException("asd");
        }
    }

    public Promise getPromise()
    {
        return this.promise;
    }
}
