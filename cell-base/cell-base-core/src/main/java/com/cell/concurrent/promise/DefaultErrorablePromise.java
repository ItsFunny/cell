package com.cell.concurrent.promise;


import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.base.BasePromise;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.GenericFutureListener;
import com.cell.concurrent.exception.ErrorWrapperException;
import com.cell.enums.CellError;

public class DefaultErrorablePromise<T> extends BasePromise<T> implements IPromise<T> {

	/**
	 * @brief 当DefaultPromise 使用该DummyExecutor初始化的时候，
	 *        那么在哪个线程给promise设的值，那个线程就会调future listener 里面的回调函数
	 */
	public DefaultErrorablePromise(EventExecutor executor) {
		super(executor);
	}

	@Override
	public IPromise<T> setSuccess(T result) {
		super.setSuccess(result);
		return this;
	}

	@Override
	public boolean trySuccess(T result) {
		return super.trySuccess(result);
	}

	/**
	 * @throws UnsupportedOperationException
	 *             if invoke it
	 */
	@Deprecated
	@Override
	public IPromise<T> setFailure(Throwable cause) {
		throw new UnsupportedOperationException("unimplemented");
	}

	/**
	 * @throws UnsupportedOperationException
	 *             if invoke it
	 */
	@Deprecated
	@Override
	public boolean tryFailure(Throwable cause) {
		throw new UnsupportedOperationException("unimplemented");
	}

	@Override
	public IPromise<T> addListener(GenericFutureListener<? extends Future<? super T>> listener) {
		super.addListener(listener);
		return this;
	}

	@Override
	public IPromise<T> addListeners(@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super T>>... listeners) {
		super.addListeners(listeners);
		return this;
	}

	@Override
	public IPromise<T> removeListener(GenericFutureListener<? extends Future<? super T>> listener) {
		super.removeListener(listener);
		return this;
	}

	@Override
	public IPromise<T> removeListeners(@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super T>>... listeners) {
		super.removeListeners(listeners);
		return this;
	}

	@Override
	public IPromise<T> sync() throws InterruptedException
    {
		super.sync();
		return this;
	}

	@Override
	public IPromise<T> syncUninterruptibly() {
		super.syncUninterruptibly();
		return this;
	}

	@Override
	public IPromise<T> await() throws InterruptedException
    {
		super.await();
		return this;
	}

	@Override
	public IPromise<T> awaitUninterruptibly() {
		super.awaitUninterruptibly();
		return this;
	}

	@Override
	protected void checkDeadLock() {
		// 如果是dummy executor就不检查deadlock
		if (this.executor() instanceof DummyExecutor) {
			return;
		} else {
			super.checkDeadLock();
		}
	}

	@Override
	public IPromise<T> setFailure(CellError error) {
		super.setFailure(new ErrorWrapperException(error));
		return this;
	}

	@Override
	public boolean tryFailure(CellError error) {
		return super.tryFailure(new ErrorWrapperException(error));
	}

	@Override
	public CellError getError() {
		if (super.cause() instanceof ErrorWrapperException) {
			ErrorWrapperException cause = (ErrorWrapperException) super.cause();
			return cause.getErrorCode();
		} else if (super.cause() != null) {
			return CellError.UNKNOWN_ERROR;
		} else {
			return CellError.NO_ERROR;
		}
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public IFuture<T> addListener(IFutureListener<T> listener) {
		super.addListener(listener);
		return this;
	}
}

