package com.cell.concurrent;


import com.cell.concurrent.base.EventLoop;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.concurrent.base.SingleThreadEventExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;


abstract class BSSingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop
{

    protected BSSingleThreadEventLoop(EventLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
        super(parent, threadFactory, addTaskWakesUp);
    }

    protected BSSingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp) {
        super(parent, executor, addTaskWakesUp);
    }

    @Override
    public EventLoopGroup parent() {
        return (EventLoopGroup) super.parent();
    }

    @Override
    public EventLoop next() {
        return (EventLoop) super.next();
    }


    @Override
    protected boolean wakesUpForTask(Runnable task) {
        return !(task instanceof NonWakeupRunnable);
    }

    @Override
    public final int executorCount() {
    	return 1;
    }
    /**
     * Marker interface for {@link Runnable} that will not trigger an {@link #wakeup(boolean)} in all cases.
     */
    interface NonWakeupRunnable extends Runnable { }
}


