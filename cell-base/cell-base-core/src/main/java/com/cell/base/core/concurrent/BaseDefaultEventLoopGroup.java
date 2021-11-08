package com.cell.base.core.concurrent;


import com.cell.base.core.concurrent.base.EventLoop;
import com.cell.base.core.concurrent.base.MultithreadEventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;


public class BaseDefaultEventLoopGroup extends MultithreadEventLoopGroup
{

    /**
     * Create a new instance with the default number of threads.
     */
    public BaseDefaultEventLoopGroup()
    {
        this(0);
    }

    /**
     * Create a new instance
     *
     * @param nThreads the number of threads to use
     */
    public BaseDefaultEventLoopGroup(int nThreads)
    {
        this(nThreads, null);
    }

    /**
     * Create a new instance
     *
     * @param nThreads      the number of threads to use
     * @param threadFactory the {@link ThreadFactory} or {@code null} to use the default
     */
    public BaseDefaultEventLoopGroup(int nThreads, ThreadFactory threadFactory)
    {
        super(nThreads, threadFactory);
    }

    @Override
    protected EventLoop newChild(Executor executor, Object... args) throws Exception
    {
        return new BaseDefaultEventLoop(this, executor);
    }
}

