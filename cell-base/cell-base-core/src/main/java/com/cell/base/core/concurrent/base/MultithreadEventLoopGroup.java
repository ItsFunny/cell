package com.cell.base.core.concurrent.base;


import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Abstract base class for {@link EventLoopGroup} implementations that handles their tasks with multiple threads at
 * the same time.
 */
public abstract class MultithreadEventLoopGroup extends MultithreadEventExecutorGroup implements EventLoopGroup
{


    private static final int DEFAULT_EVENT_LOOP_THREADS;

    static
    {
        DEFAULT_EVENT_LOOP_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * @see {@link MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, Executor, Object...)}
     */
    protected MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args)
    {
        super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args);
    }

    /**
     * @see {@link MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, ThreadFactory, Object...)}
     */
    protected MultithreadEventLoopGroup(int nThreads, ThreadFactory threadFactory, Object... args)
    {
        super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, threadFactory, args);
    }

    /**
     * @see {@link MultithreadEventExecutorGroup#MultithreadEventExecutorGroup(int, Executor,
     * EventExecutorChooserFactory, Object...)}
     */
    protected MultithreadEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory,
                                        Object... args)
    {
        super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, chooserFactory, args);
    }

    @Override
    protected ThreadFactory newDefaultThreadFactory()
    {
        return new DefaultThreadFactory(getClass(), Thread.MAX_PRIORITY);
    }

    @Override
    public EventLoop next()
    {
        return (EventLoop) super.next();
    }

    @Override
    protected abstract EventLoop newChild(Executor executor, Object... args) throws Exception;

}
