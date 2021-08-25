package com.cell.concurrent.promise;


import com.cell.concurrent.base.*;
import com.cell.concurrent.exception.PromiseTimeoutException;

import java.util.concurrent.TimeUnit;

public class BSTimeoutPromise extends BasePromise<Object> implements BSPromise {
	private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
	private volatile ScheduledFuture<?> timeoutFuture;
	private volatile long initNanos;
	private volatile long timeoutNanos;

	/**
	 * @brief 超时的promise 绝对不能用OrigineDummyExecutor
	 * @param executor
	 */
	public BSTimeoutPromise(EventExecutor executor) {
		super(executor);
		initNanos = 0;
	}
	
	public BSTimeoutPromise(EventExecutor executor, long timeout, TimeUnit timeUnit) {
		super(executor);
		initNanos = 0;
		setTimeout(timeout, timeUnit);
	}

	@Override
	public void setTimeout(long timeout, TimeUnit timeUnit) {
		if (initNanos != 0) {
			throw new IllegalStateException("promise setTimeout只能被设置一次");
		}
		initNanos = System.nanoTime();
		if (timeout <= 0) {
			timeoutNanos = 0;
		} else {
			timeoutNanos = Math.max(timeUnit.toNanos(timeout), MIN_TIMEOUT_NANOS);
		}
		timeoutFuture = executor().schedule(new Runnable() {
			@Override
			public void run() {
				if(isDone()) {
					return;
				}
				long nextDelay = timeoutNanos;
				nextDelay -= System.nanoTime() - initNanos;
				if (nextDelay <= 0) {
					// 如果在规定时间内，用户状态还不是已登录抛出异常
					tryFailure(new PromiseTimeoutException());
				} else {
					// 如果提前触发了，则继续延后触发
					executor().schedule(this, nextDelay, TimeUnit.NANOSECONDS);
				}
			}
		}, timeout, timeUnit);
	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BSPromise setSuccess(Object result) {
		super.setSuccess(result);
		return this;
	}

	@Override
	public BSPromise setSuccess() {
		trySuccess();
		return this;
	}

	@Override
	public boolean trySuccess() {
		return trySuccess(null);
	}

	@Override
	public BSPromise setFailure(Throwable cause) {
		tryFailure(cause);
		return this;
	}

	@Override
	public BSPromise addListener(GenericFutureListener<? extends Future<? super Object>> listener) {
		super.addListener(listener);
		return this;
	}

	@Override
	public BSPromise addListeners(GenericFutureListener<? extends Future<? super Object>>... listeners) {
		super.addListeners(listeners);
		return this;
	}

	@Override
	public BSPromise removeListener(GenericFutureListener<? extends Future<? super Object>> listener) {
		super.removeListener(listener);
		return this;
	}

	@Override
	public BSPromise removeListeners(GenericFutureListener<? extends Future<? super Object>>... listeners) {
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
	public BSPromise syncUninterruptibly() {
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
	public BSPromise awaitUninterruptibly() {
		super.awaitUninterruptibly();
		return this;
	}
}
