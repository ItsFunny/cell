package com.cell.concurrent.promise;


import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;

public interface BaseFuture extends Future<Object>
{

    @Override
    BaseFuture addListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BaseFuture addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BaseFuture removeListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BaseFuture removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BaseFuture sync() throws InterruptedException;

    @Override
    BaseFuture syncUninterruptibly();

    @Override
    BaseFuture await() throws InterruptedException;

    @Override
    BaseFuture awaitUninterruptibly();

    EventExecutor executor();

    /**
     * following methods:
     * <ul>
     * <li>{@link #addListener(GenericFutureListener)}</li>
     * <li>{@link #addListeners(GenericFutureListener[])}</li>
     * <li>{@link #await()}</li>
     * <li>{@link #await(long)} ()}</li>
     * <li>{@link #awaitUninterruptibly()}</li>
     * <li>{@link #sync()}</li>
     * <li>{@link #syncUninterruptibly()}</li>
     * </ul>
     */
    boolean isNull();
}

