package com.cell.protocol.impl;

import com.cell.concurrent.base.Promise;
import com.cell.protocol.IServerResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 12:39
 */
public abstract class AbstractBaseResponse implements IServerResponse
{
    private Promise<Object> promise;

    @Override
    public void setPromise(Promise<Object> promise)
    {
        this.promise = promise;
    }

    @Override
    public Promise<Object> getPromise()
    {
        return this.promise;
    }

    @Override
    public void fireResult(Object o)
    {
        this.promise.trySuccess(o);
    }

    @Override
    public void fireFailure(Exception e)
    {
        this.promise.tryFailure(e);
    }

    @Override
    public boolean isSetOrExpired()
    {
        return promise.isDone() || !promise.isCancelled();
    }
}
