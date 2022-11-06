package com.cell.plugin.develop.context.event;

import com.cell.base.common.events.IEvent;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.Data;


public class PromiseWrapper<T> implements IEvent
{
    private Promise<T> promise;
    private boolean handled;

    private IEvent data;

    public PromiseWrapper(IEvent data)
    {
        this.data = data;
        this.promise = new DefaultPromise<>(JobCenter.getInstance().getEventExecutors().next());
    }

    public IEvent getData()
    {
        return data;
    }

    public void handle()
    {
        this.handled = true;
    }
     boolean isHandled()
    {
        return this.handled;
    }

    public void done(T t)
    {
        this.promise.trySuccess(t);
    }

    public T waitUntil()
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
