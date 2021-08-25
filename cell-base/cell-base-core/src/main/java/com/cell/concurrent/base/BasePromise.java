package com.cell.concurrent.base;


import com.cell.concurrent.exception.BlockingOperationException;
import com.cell.concurrent.internal.InternalThreadLocalMap;
import com.cell.log.LOG;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@SuppressWarnings("rawtypes")
public class BasePromise<V> extends AbstractFuture<V> implements Promise<V>
{
    private static final int MAX_LISTENER_STACK_DEPTH = 8;
    private static final AtomicReferenceFieldUpdater<BasePromise, Object> RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(BasePromise.class, Object.class, "result");
    private static final String SUCCESS = new String(BasePromise.class.getName() + '#' + "SUCCESS");
    private static final String UNCANCELLABLE = new String(BasePromise.class.getName() + '#' + "UNCANCELLABLE");
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(unknownStackTrace(new CancellationException(), BasePromise.class, "cancel(...)"));

    public static <T extends Throwable> T unknownStackTrace(T cause, Class<?> clazz, String method)
    {
        cause.setStackTrace(new StackTraceElement[]{new StackTraceElement(clazz.getName(), method, null, -1)});
        return cause;
    }

    private volatile Object result;
    private final EventExecutor executor;
    /**
     * One or more listeners. Can be a {@link GenericFutureListener} or a {@link BasePromiseListeners}.
     * If {@code null}, it means either 1) no listeners were added yet or 2) all listeners were notified.
     * <p>
     * Threading - synchronized(this). We must support adding listeners when there is no EventExecutor.
     */
    private Object listeners;
    /**
     * Threading - synchronized(this). We are required to hold the monitor to use Java's underlying wait()/notifyAll().
     */
    private short waiters;

    /**
     * Threading - synchronized(this). We must prevent concurrent notification and FIFO listener notification if the
     * executor changes.
     */
    private boolean notifyingListeners;

    /**
     * Creates a new instance.
     * <p>
     * It is preferable to use {@link EventExecutor#newPromise()} to create a new promise
     *
     * @param executor the {@link EventExecutor} which is used to notify the promise once it is complete.
     *                 It is assumed this executor will protect against {@link StackOverflowError} exceptions.
     *                 The executor may be used to avoid {@link StackOverflowError} by executing a {@link Runnable} if the stack
     *                 depth exceeds a threshold.
     */
    public BasePromise(EventExecutor executor)
    {
        this.executor = checkNotNull(executor, "executor");
    }

    public EventExecutor executor()
    {
        return this.executor;
    }

    /**
     * See {@link #executor()} for expectations of the executor.
     */
    protected BasePromise()
    {
        // only for subclasses
        executor = null;
    }

    @Override
    public Promise<V> setSuccess(V result)
    {
        if (setSuccess0(result))
        {
            notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this);
    }

    @Override
    public boolean trySuccess(V result)
    {
        if (setSuccess0(result))
        {
            notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public Promise<V> setFailure(Throwable cause)
    {
        if (setFailure0(cause))
        {
            notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this, cause);
    }

    @Override
    public boolean tryFailure(Throwable cause)
    {
        if (setFailure0(cause))
        {
            notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public boolean setUncancellable()
    {
        if (RESULT_UPDATER.compareAndSet(this, null, UNCANCELLABLE))
        {
            return true;
        }
        Object result = this.result;
        return !isDone0(result) || !isCancelled0(result);
    }

    @Override
    public boolean isSuccess()
    {
        Object result = this.result;
        return result != null && result != UNCANCELLABLE && !(result instanceof CauseHolder);
    }

    @Override
    public boolean isCancellable()
    {
        return result == null;
    }

    @Override
    public Throwable cause()
    {
        Object result = this.result;
        return (result instanceof CauseHolder) ? ((CauseHolder) result).cause : null;
    }

    @Override
    public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener)
    {
        checkNotNull(listener, "listener");

        synchronized (this)
        {
            addListener0(listener);
        }

        if (isDone())
        {
            notifyListeners();
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners)
    {
        checkNotNull(listeners, "listeners");

        synchronized (this)
        {
            for (GenericFutureListener<? extends Future<? super V>> listener : listeners)
            {
                if (listener == null)
                {
                    break;
                }
                addListener0(listener);
            }
        }

        if (isDone())
        {
            notifyListeners();
        }

        return this;
    }

    @Override
    public Promise<V> removeListener(final GenericFutureListener<? extends Future<? super V>> listener)
    {
        checkNotNull(listener, "listener");

        synchronized (this)
        {
            removeListener0(listener);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Promise<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... listeners)
    {
        checkNotNull(listeners, "listeners");

        synchronized (this)
        {
            for (GenericFutureListener<? extends Future<? super V>> listener : listeners)
            {
                if (listener == null)
                {
                    break;
                }
                removeListener0(listener);
            }
        }

        return this;
    }

    @Override
    public Promise<V> await() throws InterruptedException
    {
        if (isDone())
        {
            return this;
        }

        if (Thread.interrupted())
        {
            throw new InterruptedException(toString());
        }

        checkDeadLock();

        synchronized (this)
        {
            while (!isDone())
            {
                incWaiters();
                try
                {
                    wait();
                } finally
                {
                    decWaiters();
                }
            }
        }
        return this;
    }

    @Override
    public Promise<V> awaitUninterruptibly()
    {
        if (isDone())
        {
            return this;
        }

        checkDeadLock();

        boolean interrupted = false;
        synchronized (this)
        {
            while (!isDone())
            {
                incWaiters();
                try
                {
                    wait();
                } catch (InterruptedException e)
                {
                    // Interrupted while waiting.
                    interrupted = true;
                } finally
                {
                    decWaiters();
                }
            }
        }

        if (interrupted)
        {
            Thread.currentThread().interrupt();
        }

        return this;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException
    {
        return await0(unit.toNanos(timeout), true);
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException
    {
        return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
    {
        try
        {
            return await0(unit.toNanos(timeout), false);
        } catch (InterruptedException e)
        {
            // Should not be raised at all.
            throw new InternalError();
        }
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis)
    {
        try
        {
            return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
        } catch (InterruptedException e)
        {
            // Should not be raised at all.
            throw new InternalError();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getNow()
    {
        Object result = this.result;
        if (result instanceof CauseHolder || result == SUCCESS)
        {
            return null;
        }
        return (V) result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        if (RESULT_UPDATER.compareAndSet(this, null, CANCELLATION_CAUSE_HOLDER))
        {
            checkNotifyWaiters();
            notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public boolean isCancelled()
    {
        return isCancelled0(result);
    }

    @Override
    public boolean isDone()
    {
        return isDone0(result);
    }

    @Override
    public Promise<V> sync() throws InterruptedException
    {
        await();
        rethrowIfFailed();
        return this;
    }

    @Override
    public Promise<V> syncUninterruptibly()
    {
        awaitUninterruptibly();
        rethrowIfFailed();
        return this;
    }

    @Override
    public String toString()
    {
        return toStringBuilder().toString();
    }

    protected StringBuilder toStringBuilder()
    {
        StringBuilder buf = new StringBuilder(64)
                .append(StringUtil.simpleClassName(this))
                .append('@')
                .append(Integer.toHexString(hashCode()));

        Object result = this.result;
        if (result == SUCCESS)
        {
            buf.append("(success)");
        } else if (result == UNCANCELLABLE)
        {
            buf.append("(uncancellable)");
        } else if (result instanceof CauseHolder)
        {
            buf.append("(failure: ")
                    .append(((CauseHolder) result).cause)
                    .append(')');
        } else if (result != null)
        {
            buf.append("(success: ")
                    .append(result)
                    .append(')');
        } else
        {
            buf.append("(incomplete)");
        }

        return buf;
    }

    /**
     * Get the executor used to notify listeners when this promise is complete.
     * <p>
     * It is assumed this executor will protect against {@link StackOverflowError} exceptions.
     * The executor may be used to avoid {@link StackOverflowError} by executing a {@link Runnable} if the stack
     * depth exceeds a threshold.
     *
     * @return The executor used to notify listeners when this promise is complete.
     */

    protected void checkDeadLock()
    {
        EventExecutor e = executor();
        if (e != null && e.inEventLoop())
        {
            throw new BlockingOperationException(toString());
        }
    }

    /**
     * Notify a listener that a future has completed.
     * <p>
     * This method has a fixed depth of {@link #MAX_LISTENER_STACK_DEPTH} that will limit recursion to prevent
     * {@link StackOverflowError} and will stop notifying listeners added after this threshold is exceeded.
     *
     * @param eventExecutor the executor to use to notify the listener {@code listener}.
     * @param future        the future that is complete.
     * @param listener      the listener to notify.
     */
    protected static void notifyListener(
            EventExecutor eventExecutor, final Future<?> future, final GenericFutureListener<?> listener)
    {
        checkNotNull(eventExecutor, "eventExecutor");
        checkNotNull(future, "future");
        checkNotNull(listener, "listener");
        notifyListenerWithStackOverFlowProtection(eventExecutor, future, listener);
    }

    private void notifyListeners()
    {
        // Modifications to listeners should be done in a synchronized block before this, and should be visible here.
        if (listeners == null)
        {
            return;
        }
        notifyListenersWithStackOverFlowProtection();
    }

    private void notifyListenersWithStackOverFlowProtection()
    {
        EventExecutor executor = executor();
        if (executor.inEventLoop())
        {
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < MAX_LISTENER_STACK_DEPTH)
            {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try
                {
                    notifyListenersNow();
                } finally
                {
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }

        safeExecute(executor, new Runnable()
        {
            @Override
            public void run()
            {
                notifyListenersNow();
            }
        });
    }

    /**
     * The logic in this method should be identical to {@link #notifyListenersWithStackOverFlowProtection()} but
     * listener(s) may be changed and is protected by a synchronized operation.
     */
    private static void notifyListenerWithStackOverFlowProtection(final EventExecutor executor,
                                                                  final Future<?> future,
                                                                  final GenericFutureListener<?> listener)
    {
        if (executor.inEventLoop())
        {
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.futureListenerStackDepth();
            if (stackDepth < MAX_LISTENER_STACK_DEPTH)
            {
                threadLocals.setFutureListenerStackDepth(stackDepth + 1);
                try
                {
                    notifyListener0(future, listener);
                } finally
                {
                    threadLocals.setFutureListenerStackDepth(stackDepth);
                }
                return;
            }
        }

        safeExecute(executor, new Runnable()
        {
            @Override
            public void run()
            {
                notifyListener0(future, listener);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void notifyListenersNow()
    {
        Object listeners;
        synchronized (this)
        {
            // Only proceed if there are listeners to notify and we are not already notifying listeners.
            if (notifyingListeners || this.listeners == null)
            {
                return;
            }
            notifyingListeners = true;
            listeners = this.listeners;
            this.listeners = null;
        }
        for (; ; )
        {
            if (listeners instanceof BasePromiseListeners)
            {
                notifyListeners0((BasePromiseListeners) listeners);
            } else
            {
                notifyListener0(this, (GenericFutureListener<? extends Future<V>>) listeners);
            }
            synchronized (this)
            {
                if (this.listeners == null)
                {
                    // Nothing can throw from within this method, so setting notifyingListeners back to false does not
                    // need to be in a finally block.
                    notifyingListeners = false;
                    return;
                }
                listeners = this.listeners;
                this.listeners = null;
            }
        }
    }

    private void notifyListeners0(BasePromiseListeners listeners)
    {
        GenericFutureListener<?>[] a = listeners.listeners();
        int size = listeners.size();
        for (int i = 0; i < size; i++)
        {
            notifyListener0(this, a[i]);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static void notifyListener0(Future future, GenericFutureListener l)
    {
        try
        {
            l.operationComplete(future);
        } catch (Throwable t)
        {
            LOG.warn("Promise notify listener An exception was thrown by {}.operationComplete()");
//            LOG.warning(BSModule.COMMON, t, "Promise notify listener An exception was thrown by {}.operationComplete()");
        }
    }

    @SuppressWarnings("unchecked")
    private void addListener0(GenericFutureListener<? extends Future<? super V>> listener)
    {
        if (listeners == null)
        {
            listeners = listener;
        } else if (listeners instanceof BasePromiseListeners)
        {
            ((BasePromiseListeners) listeners).add(listener);
        } else
        {
            listeners = new BasePromiseListeners((GenericFutureListener<? extends Future<V>>) listeners, listener);
        }
    }

    private void removeListener0(GenericFutureListener<? extends Future<? super V>> listener)
    {
        if (listeners instanceof BasePromiseListeners)
        {
            ((BasePromiseListeners) listeners).remove(listener);
        } else if (listeners == listener)
        {
            listeners = null;
        }
    }

    private boolean setSuccess0(V result)
    {
        return setValue0(result == null ? SUCCESS : result);
    }

    private boolean setFailure0(Throwable cause)
    {
        return setValue0(new CauseHolder(checkNotNull(cause, "cause")));
    }

    private boolean setValue0(Object objResult)
    {
        if (RESULT_UPDATER.compareAndSet(this, null, objResult) ||
                RESULT_UPDATER.compareAndSet(this, UNCANCELLABLE, objResult))
        {
            checkNotifyWaiters();
            return true;
        }
        return false;
    }

    private synchronized void checkNotifyWaiters()
    {
        if (waiters > 0)
        {
            notifyAll();
        }
    }

    private void incWaiters()
    {
        if (waiters == Short.MAX_VALUE)
        {
            throw new IllegalStateException("too many waiters: " + this);
        }
        ++waiters;
    }

    private void decWaiters()
    {
        --waiters;
    }

    private void rethrowIfFailed()
    {
        Throwable cause = cause();
        if (cause == null)
        {
            return;
        }
        BasePromise.<RuntimeException>throwException0(cause);
    }

    private static <E extends Throwable> void throwException0(Throwable t) throws E
    {
        throw (E) t;
    }

    private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException
    {
        if (isDone())
        {
            return true;
        }

        if (timeoutNanos <= 0)
        {
            return isDone();
        }

        if (interruptable && Thread.interrupted())
        {
            throw new InterruptedException(toString());
        }

        checkDeadLock();

        long startTime = System.nanoTime();
        long waitTime = timeoutNanos;
        boolean interrupted = false;
        try
        {
            for (; ; )
            {
                synchronized (this)
                {
                    incWaiters();
                    try
                    {
                        wait(waitTime / 1000000, (int) (waitTime % 1000000));
                    } catch (InterruptedException e)
                    {
                        if (interruptable)
                        {
                            throw e;
                        } else
                        {
                            interrupted = true;
                        }
                    } finally
                    {
                        decWaiters();
                    }
                }
                if (isDone())
                {
                    return true;
                } else
                {
                    waitTime = timeoutNanos - (System.nanoTime() - startTime);
                    if (waitTime <= 0)
                    {
                        return isDone();
                    }
                }
            }
        } finally
        {
            if (interrupted)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Notify all progressive listeners.
     * <p>
     * No attempt is made to ensure notification order if multiple calls are made to this method before
     * the original invocation completes.
     * <p>
     *
     * @param progress the new progress.
     * @param total    the total progress.
     */
    @SuppressWarnings("unchecked")
    void notifyProgressiveListeners(final long progress, final long total)
    {
    }


    private static boolean isCancelled0(Object result)
    {
        return result instanceof CauseHolder && ((CauseHolder) result).cause instanceof CancellationException;
    }

    private static boolean isDone0(Object result)
    {
        return result != null && result != UNCANCELLABLE;
    }

    private static final class CauseHolder
    {
        final Throwable cause;

        CauseHolder(Throwable cause)
        {
            this.cause = cause;
        }
    }

    private static void safeExecute(EventExecutor executor, Runnable task)
    {
        try
        {
            executor.execute(task);
        } catch (Throwable t)
        {
//            LOG.warning(BSModule.COMMON, t, "VWPromise Failed to submit a listener notification task. Event loop shut down?");
        }
    }

    public static <T> T checkNotNull(T arg, String text)
    {
        if (arg == null)
        {
            throw new NullPointerException(text);
        }
        return arg;
    }
}
