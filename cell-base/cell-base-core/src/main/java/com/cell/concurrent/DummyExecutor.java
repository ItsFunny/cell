package com.cell.concurrent;


import com.cell.concurrent.base.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class DummyExecutor implements EventExecutor
{

    private final static DummyExecutor DUMMY_EXECUTOR = new DummyExecutor();

    private DummyExecutor() {}

    public static DummyExecutor getInstance()
    {
        return DUMMY_EXECUTOR;
    }

    @Override
    public boolean inEventLoop(Thread thread)
    {
        return false;
    }

    @Override
    public final int executorCount()
    {
        return 1;
    }

    @Override
    public void execute(Runnable command)
    {
        try
        {
            command.run();
        } catch (Throwable t)
        {
            exceptionCaughtInTask(command, t);
        }
    }

    protected void exceptionCaughtInTask(Runnable task, Throwable e)
    {
//		LOG.warning(Module.COMMON, e, "exception caught in task: [%s]", task);
    }

    @Override
    public boolean inEventLoop()
    {
        return false;
    }

    @Override
    public boolean isShuttingDown()
    {
        return false;
    }

    @Override
    public boolean isShutdown()
    {
        return false;
    }

    @Override
    public boolean isTerminated()
    {
        return false;
    }

    @Override
    public Future<?> shutdownGracefully()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<?> terminationFuture()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Runnable> shutdownNow()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<EventExecutor> iterator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Future<?> submit(Runnable task)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout,
//                                         TimeUnit unit) throws InterruptedException
//    {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public EventExecutor next()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public EventExecutorGroup parent()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public <V> Promise<V> newPromise()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public <V> Future<V> newSucceededFuture(V result)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> Future<V> newFailedFuture(Throwable cause)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int pendingTasks()
    {
        return 0;
    }

    @Override
    public Thread getThread()
    {
        return null;
    }

}

