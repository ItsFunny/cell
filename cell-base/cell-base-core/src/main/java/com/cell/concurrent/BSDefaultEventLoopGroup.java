package com.cell.concurrent;


import com.cell.concurrent.base.EventLoop;
import com.cell.concurrent.base.MultithreadEventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;


public class BSDefaultEventLoopGroup extends MultithreadEventLoopGroup
{

    /**
     * Create a new instance with the default number of threads.
     */
    public BSDefaultEventLoopGroup() {
        this(0);
    }

    /**
     * Create a new instance
     *
     * @param nThreads          the number of threads to use
     */
    public BSDefaultEventLoopGroup(int nThreads) {
        this(nThreads, null);
    }

    /**
     * Create a new instance
     *
     * @param nThreads          the number of threads to use
     * @param threadFactory     the {@link ThreadFactory} or {@code null} to use the default
     */
    public BSDefaultEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
        super(nThreads, threadFactory);
    }

    @Override
    protected EventLoop newChild(Executor executor, Object... args) throws Exception
    {
        return new BSDefaultEventLoop(this, executor);
    }
}

