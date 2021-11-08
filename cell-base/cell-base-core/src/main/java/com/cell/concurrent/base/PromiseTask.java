package com.cell.concurrent.base;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

public class PromiseTask<V> extends BasePromise<V> implements RunnableFuture<V>
{

    static <T> Callable<T> toCallable(Runnable runnable, T result)
    {
        return new RunnableAdapter<T>(runnable, result);
    }

    private static final class RunnableAdapter<T> implements Callable<T>
    {
        final Runnable task;
        final T result;

        RunnableAdapter(Runnable task, T result)
        {
            this.task = task;
            this.result = result;
        }

        @Override
        public T call()
        {
            task.run();
            return result;
        }

        @Override
        public String toString()
        {
            return "Callable(task: " + task + ", result: " + result + ')';
        }
    }

    protected final Callable<V> task;

    PromiseTask(EventExecutor executor, Runnable runnable, V result)
    {
        this(executor, toCallable(runnable, result));
    }

    PromiseTask(EventExecutor executor, Callable<V> callable)
    {
        super(executor);
        task = callable;
    }

    @Override
    public final int hashCode()
    {
        return System.identityHashCode(this);
    }

    @Override
    public final boolean equals(Object obj)
    {
        return this == obj;
    }

    @Override
    public void run()
    {
        try
        {
            if (setUncancellableInternal())
            {
                V result = task.call();
                setSuccessInternal(result);
            }
        } catch (Throwable e)
        {
            setFailureInternal(e);
        }
    }

    @Override
    public final Promise<V> setFailure(Throwable cause)
    {
        throw new IllegalStateException();
    }

    protected final Promise<V> setFailureInternal(Throwable cause)
    {
        super.setFailure(cause);
        return this;
    }

    @Override
    public final boolean tryFailure(Throwable cause)
    {
        return false;
    }

    protected final boolean tryFailureInternal(Throwable cause)
    {
        return super.tryFailure(cause);
    }

    @Override
    public final Promise<V> setSuccess(V result)
    {
        throw new IllegalStateException();
    }

    protected final Promise<V> setSuccessInternal(V result)
    {
        super.setSuccess(result);
        return this;
    }

    @Override
    public final boolean trySuccess(V result)
    {
        return false;
    }

    protected final boolean trySuccessInternal(V result)
    {
        return super.trySuccess(result);
    }

    @Override
    public final boolean setUncancellable()
    {
        throw new IllegalStateException();
    }

    protected final boolean setUncancellableInternal()
    {
        return super.setUncancellable();
    }

    @Override
    protected StringBuilder toStringBuilder()
    {
        StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, ',');

        return buf.append(" task: ")
                .append(task)
                .append(')');
    }
}
