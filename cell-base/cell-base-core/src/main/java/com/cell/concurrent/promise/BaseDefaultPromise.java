package com.cell.concurrent.promise;

import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;

import java.util.concurrent.TimeUnit;

public class BaseDefaultPromise extends com.cell.concurrent.base.BasePromise<Object> implements BasePromise
{
    /**
     * @brief 当OrigineDefaultPromise 使用该OrigineDummyExecutor初始化的时候，
     * 那么在哪个线程给promise设的值，那个线程就会调future listener
     * 里面的回调函数
     */
    public BaseDefaultPromise(EventExecutor executor)
    {
        super(executor);
    }

    @Override
    public void setTimeout(long timeout, TimeUnit timeUnit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNull()
    {
        return false;
    }

    @Override
    public BasePromise setSuccess(Object result)
    {
        super.setSuccess(result);
        return this;
    }

    @Override
    public BasePromise setSuccess()
    {
        return setSuccess(null);
    }

    @Override
    public boolean trySuccess()
    {
        return trySuccess(null);
    }

    @Override
    public BasePromise setFailure(Throwable cause)
    {
        super.setFailure(cause);
        return this;
    }

    @Override
    public BasePromise addListener(GenericFutureListener<? extends Future<? super Object>> listener)
    {
        super.addListener(listener);
        return this;
    }

    @Override
    public BasePromise addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners)
    {
        super.addListeners(listeners);
        return this;
    }

    @Override
    public BasePromise removeListener(GenericFutureListener<? extends Future<? super Object>> listener)
    {
        super.removeListener(listener);
        return this;
    }

    @Override
    public BasePromise removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners)
    {
        super.removeListeners(listeners);
        return this;
    }

    @Override
    public BasePromise sync() throws InterruptedException
    {
        super.sync();
        return this;
    }

    @Override
    public BasePromise syncUninterruptibly()
    {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public BasePromise await() throws InterruptedException
    {
        super.await();
        return this;
    }

    @Override
    public BasePromise awaitUninterruptibly()
    {
        super.awaitUninterruptibly();
        return this;
    }

    @Override
    protected void checkDeadLock()
    {
        //如果是dummy executor就不检查deadlock
        if (this.executor() instanceof DummyExecutor)
        {
            return;
        } else
        {
            super.checkDeadLock();
        }
    }

}

