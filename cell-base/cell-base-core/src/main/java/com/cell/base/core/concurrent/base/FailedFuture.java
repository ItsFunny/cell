package com.cell.base.core.concurrent.base;

/**
 * The {@link CompleteFuture} which is failed already.  It is
 * recommended to use {@link EventExecutor#newFailedFuture(Throwable)}
 * instead of calling the constructor of this future.
 */
public final class FailedFuture<V> extends CompleteFuture<V>
{

    private final Throwable cause;

    /**
     * Creates a new instance.
     *
     * @param executor the {@link EventExecutor} associated with this future
     * @param cause    the cause of failure
     */
    public FailedFuture(EventExecutor executor, Throwable cause)
    {
        super(executor);
        if (cause == null)
        {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }

    @Override
    public Throwable cause()
    {
        return cause;
    }

    @Override
    public boolean isSuccess()
    {
        return false;
    }

    @Override
    public Future<V> sync()
    {
        FailedFuture.<RuntimeException>throwException0(cause);
        return this;
    }

    @Override
    public Future<V> syncUninterruptibly()
    {
        FailedFuture.<RuntimeException>throwException0(cause);
        return this;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwException0(Throwable t) throws E
    {
        throw (E) t;
    }

    @Override
    public V getNow()
    {
        return null;
    }
}

