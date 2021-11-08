package com.cell.base.core.concurrent.promise;


import com.cell.base.core.concurrent.base.Future;
import com.cell.base.core.concurrent.base.GenericFutureListener;
import com.cell.base.core.concurrent.base.Promise;

import java.util.concurrent.TimeUnit;

public interface BasePromise extends BaseFuture, Promise<Object>
{

    @Override
    BasePromise setSuccess(Object result);

    BasePromise setSuccess();

    /**
     * @param timeout
     * @param timeUnit
     * @brief 设置 promise超时，如果promise在指定时间内没有被设置上值，那么promise将setCause(PromiseTimeoutException)
     */
    void setTimeout(long timeout, TimeUnit timeUnit);

    boolean trySuccess();

    @Override
    BasePromise setFailure(Throwable cause);

    @Override
    BasePromise addListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BasePromise addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BasePromise removeListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BasePromise removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BasePromise sync() throws InterruptedException;

    @Override
    BasePromise syncUninterruptibly();

    @Override
    BasePromise await() throws InterruptedException;

    @Override
    BasePromise awaitUninterruptibly();

}

