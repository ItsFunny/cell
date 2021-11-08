package com.cell.concurrent;


import com.cell.concurrent.base.DefaultThreadFactory;
import com.cell.concurrent.base.EventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class BaseDefaultEventLoop extends BaseSingleThreadEventLoop
{
    public BaseDefaultEventLoop()
    {
        this((EventLoopGroup) null);
    }

    public BaseDefaultEventLoop(ThreadFactory threadFactory)
    {
        this(null, threadFactory);
    }

    public BaseDefaultEventLoop(Executor executor)
    {
        this(null, executor);
    }

    public BaseDefaultEventLoop(EventLoopGroup parent)
    {
        this(parent, new DefaultThreadFactory(BaseDefaultEventLoop.class));
    }

    public BaseDefaultEventLoop(EventLoopGroup parent, ThreadFactory threadFactory)
    {
        super(parent, threadFactory, true);
    }

    public BaseDefaultEventLoop(EventLoopGroup parent, Executor executor)
    {
        super(parent, executor, true);
    }

    @Override
    protected void run()
    {
        for (; ; )
        {
            Runnable task = takeTask();
            if (task != null)
            {
                task.run();
                updateLastExecutionTime();
            }

            if (confirmShutdown())
            {
                break;
            }
        }
    }
}
