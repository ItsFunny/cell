package com.cell.base.core.concurrent.promise;


import com.cell.base.core.concurrent.base.EventExecutor;
import com.cell.base.core.concurrent.base.Future;
import com.cell.base.core.concurrent.base.GenericFutureListener;
import com.cell.base.core.concurrent.base.ScheduledFuture;
import com.cell.base.core.concurrent.exception.PromiseTimeoutException;

import java.util.concurrent.TimeUnit;

public class BaseTimeoutPromise extends com.cell.base.core.concurrent.base.BasePromise<Object> implements BasePromise
{
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
    private volatile ScheduledFuture<?> timeoutFuture;
    private volatile long initNanos;
    private volatile long timeoutNanos;

    /**
     * @param executor
     * @brief 超时的promise 绝对不能用OrigineDummyExecutor
     */
    public BaseTimeoutPromise(EventExecutor executor)
    {
        super(executor);
        initNanos = 0;
    }

    public BaseTimeoutPromise(EventExecutor executor, long timeout, TimeUnit timeUnit)
    {
        super(executor);
        initNanos = 0;
        setTimeout(timeout, timeUnit);
    }

    @Override
    public void setTimeout(long timeout, TimeUnit timeUnit)
    {
        if (initNanos != 0)
        {
            throw new IllegalStateException("promise setTimeout只能被设置一次");
        }
        initNanos = System.nanoTime();
        if (timeout <= 0)
        {
            timeoutNanos = 0;
        } else
        {
            timeoutNanos = Math.max(timeUnit.toNanos(timeout), MIN_TIMEOUT_NANOS);
        }
        timeoutFuture = executor().schedule(new Runnable()
        {
            @Override
            public void run()
            {
                if (isDone())
                {
                    return;
                }
                long nextDelay = timeoutNanos;
                nextDelay -= System.nanoTime() - initNanos;
                if (nextDelay <= 0)
                {
                    // 如果在规定时间内，用户状态还不是已登录抛出异常
                    tryFailure(new PromiseTimeoutException());
                } else
                {
                    // 如果提前触发了，则继续延后触发
                    executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
                }
            }
        }, timeout, timeUnit);
    }

    @Override
    public boolean isNull()
    {
        // TODO Auto-generated method stub
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
        trySuccess();
        return this;
    }

    @Override
    public boolean trySuccess()
    {
        return trySuccess(null);
    }

    @Override
    public BasePromise setFailure(Throwable cause)
    {
        tryFailure(cause);
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
}
