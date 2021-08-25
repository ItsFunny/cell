package com.cell.concurrent.promise;

import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.base.BasePromise;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;
import io.netty.channel.DefaultChannelPromise;

import java.util.concurrent.TimeUnit;

public class BSDefaultPromise extends BasePromise<Object> implements BSPromise
{
    /**
     * @brief 当OrigineDefaultPromise 使用该OrigineDummyExecutor初始化的时候，
     * 那么在哪个线程给promise设的值，那个线程就会调future listener
     * 里面的回调函数
     */
    public BSDefaultPromise(EventExecutor executor)
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
    public BSPromise setSuccess(Object result)
    {
        super.setSuccess(result);
        return this;
    }

    @Override
    public BSPromise setSuccess()
    {
        return setSuccess(null);
    }

    @Override
    public boolean trySuccess()
    {
        return trySuccess(null);
    }

    @Override
    public BSPromise setFailure(Throwable cause)
    {
        super.setFailure(cause);
        return this;
    }

    @Override
    public BSPromise addListener(GenericFutureListener<? extends Future<? super Object>> listener)
    {
        super.addListener(listener);
        return this;
    }

    @Override
    public BSPromise addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners)
    {
        super.addListeners(listeners);
        return this;
    }

    @Override
    public BSPromise removeListener(GenericFutureListener<? extends Future<? super Object>> listener)
    {
        super.removeListener(listener);
        return this;
    }

    @Override
    public BSPromise removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners)
    {
        super.removeListeners(listeners);
        return this;
    }

    @Override
    public BSPromise sync() throws InterruptedException
    {
        super.sync();
        return this;
    }

    @Override
    public BSPromise syncUninterruptibly()
    {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public BSPromise await() throws InterruptedException
    {
        super.await();
        return this;
    }

    @Override
    public BSPromise awaitUninterruptibly()
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

