package com.cell.concurrent.promise;


import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;

public interface BSFuture extends Future<Object>
{

    @Override
    BSFuture addListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BSFuture addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BSFuture removeListener(GenericFutureListener<? extends Future<? super Object>> listener);

    @Override
    BSFuture removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners);

    @Override
    BSFuture sync() throws InterruptedException;

    @Override
    BSFuture syncUninterruptibly();

    @Override
    BSFuture await() throws InterruptedException;

    @Override
    BSFuture awaitUninterruptibly();
    
    EventExecutor executor();

    /**
     * following methods:
     * <ul>
     *     <li>{@link #addListener(GenericFutureListener)}</li>
     *     <li>{@link #addListeners(GenericFutureListener[])}</li>
     *     <li>{@link #await()}</li>
     *     <li>{@link #await(long)} ()}</li>
     *     <li>{@link #awaitUninterruptibly()}</li>
     *     <li>{@link #sync()}</li>
     *     <li>{@link #syncUninterruptibly()}</li>
     * </ul>
     */
    boolean isNull();
}

