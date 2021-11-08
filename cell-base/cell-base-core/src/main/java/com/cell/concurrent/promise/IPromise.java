package com.cell.concurrent.promise;


import com.cell.base.common.enums.CellError;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;
import com.cell.concurrent.base.Promise;

import java.util.concurrent.TimeUnit;

public interface IPromise<T> extends IFuture<T>, Promise<T>
{
    IPromise<T> setFailure(CellError error);

    boolean tryFailure(CellError error);

    @Deprecated
    @Override
    IPromise<T> setFailure(Throwable cause);

    @Deprecated
    @Override
    boolean tryFailure(Throwable cause);

    @Override
    IPromise<T> setSuccess(T result);

    @Override
    IPromise<T> addListener(GenericFutureListener<? extends Future<? super T>> listener);

    @Override
    IPromise<T> addListeners(@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super T>>... listeners);

    @Override
    IPromise<T> removeListener(GenericFutureListener<? extends Future<? super T>> listener);

    @Override
    IPromise<T> removeListeners(@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super T>>... listeners);

    @Override
    IPromise<T> sync() throws InterruptedException;

    @Override
    IPromise<T> syncUninterruptibly();

    @Override
    IPromise<T> await() throws InterruptedException;

    @Override
    IPromise<T> awaitUninterruptibly();

    /**
     * following methods:
     * <ul>
     * <li>{@link #addListener(GenericFutureListener)}</li>
     * <li>{@link #addListeners(GenericFutureListener[])}</li>
     * <li>{@link #await()}</li>
     * <li>{@link #await(long, TimeUnit)} ()}</li>
     * <li>{@link #await(long)} ()}</li>
     * <li>{@link #awaitUninterruptibly()}</li>
     * <li>{@link #sync()}</li>
     * <li>{@link #syncUninterruptibly()}</li>
     * </ul>
     */
    boolean isNull();
}

