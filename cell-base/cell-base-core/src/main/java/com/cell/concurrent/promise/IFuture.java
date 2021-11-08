package com.cell.concurrent.promise;

import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;
import com.cell.enums.CellError;

public interface IFuture<T> extends Future<T>
{

    CellError getError();

    @Deprecated
    @Override
    Throwable cause();

    EventExecutor executor();

    @Override
    IFuture<T> addListener(GenericFutureListener<? extends Future<? super T>> listener);

    IFuture<T> addListener(IFutureListener<T> listener);

    @Override
    IFuture<T> addListeners(@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super T>>... listeners);

    @Override
    IFuture<T> removeListener(GenericFutureListener<? extends Future<? super T>> listener);

    @Override
    IFuture<T> removeListeners(@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super T>>... listeners);

    @Override
    IFuture<T> sync() throws InterruptedException;

    @Override
    IFuture<T> syncUninterruptibly();

    @Override
    IFuture<T> await() throws InterruptedException;

    @Override
    IFuture<T> awaitUninterruptibly();

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
