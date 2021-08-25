package com.cell.concurrent.promise;


import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;
import com.cell.concurrent.base.Promise;

import java.util.concurrent.TimeUnit;

public interface BSPromise extends BSFuture, Promise<Object>
{

    @Override
    BSPromise setSuccess(Object result);

    BSPromise setSuccess();

    /**
     * @param timeout
     * @param timeUnit
     * @brief 设置 promise超时，如果promise在指定时间内没有被设置上值，那么promise将setCause(PromiseTimeoutException)
     */
    void setTimeout(long timeout, TimeUnit timeUnit);

    boolean trySuccess();

    @Override
    BSPromise setFailure(Throwable cause);

    @Override
    BSPromise addListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BSPromise addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BSPromise removeListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BSPromise removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BSPromise sync() throws InterruptedException;

    @Override
    BSPromise syncUninterruptibly();

    @Override
    BSPromise await() throws InterruptedException;

    @Override
    BSPromise awaitUninterruptibly();

}

