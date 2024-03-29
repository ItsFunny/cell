package com.cell.base.core.concurrent.base;


import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Single-thread singleton {@link EventExecutor}.  It starts the thread automatically and stops it when there is no
 * task pending in the task queue for 1 second.  Please note it is not scalable to schedule large number of tasks to
 * this executor; use a dedicated executor.
 */
public final class GlobalEventExecutor extends AbstractScheduledEventExecutor
{

    private static final long SCHEDULE_QUIET_PERIOD_INTERVAL = TimeUnit.SECONDS.toNanos(1);

    public static final GlobalEventExecutor INSTANCE = new GlobalEventExecutor();

    final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    final ScheduledFutureTask<Void> quietPeriodTask = new ScheduledFutureTask<Void>(
            this, Executors.<Void>callable(new Runnable()
    {
        @Override
        public void run()
        {
            // NOOP
        }
    }, null), ScheduledFutureTask.deadlineNanos(SCHEDULE_QUIET_PERIOD_INTERVAL), -SCHEDULE_QUIET_PERIOD_INTERVAL);

    private final ThreadFactory threadFactory = new DefaultThreadFactory(getClass());
    private final TaskRunner taskRunner = new TaskRunner();
    private final AtomicBoolean started = new AtomicBoolean();
    volatile Thread thread;

    private final Future<?> terminationFuture = new FailedFuture<Object>(this, new UnsupportedOperationException());

    private GlobalEventExecutor()
    {
        scheduledTaskQueue().add(quietPeriodTask);
    }

    /**
     * Take the next {@link Runnable} from the task queue and so will block if no task is currently present.
     *
     * @return {@code null} if the executor thread has been interrupted or waken up.
     */
    Runnable takeTask()
    {
        BlockingQueue<Runnable> taskQueue = this.taskQueue;
        for (; ; )
        {
            ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
            if (scheduledTask == null)
            {
                Runnable task = null;
                try
                {
                    task = taskQueue.take();
                } catch (InterruptedException e)
                {
                    // Ignore
                }
                return task;
            } else
            {
                long delayNanos = scheduledTask.delayNanos();
                Runnable task;
                if (delayNanos > 0)
                {
                    try
                    {
                        task = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e)
                    {
                        // Waken up.
                        return null;
                    }
                } else
                {
                    task = taskQueue.poll();
                }

                if (task == null)
                {
                    fetchFromScheduledTaskQueue();
                    task = taskQueue.poll();
                }

                if (task != null)
                {
                    return task;
                }
            }
        }
    }

    private void fetchFromScheduledTaskQueue()
    {
        if (hasScheduledTasks())
        {
            long nanoTime = AbstractScheduledEventExecutor.nanoTime();
            for (; ; )
            {
                Runnable scheduledTask = pollScheduledTask(nanoTime);
                if (scheduledTask == null)
                {
                    break;
                }
                taskQueue.add(scheduledTask);
            }
        }
    }

    /**
     * Return the number of tasks that are pending for processing.
     *
     * <strong>Be aware that this operation may be expensive as it depends on the internal implementation of the
     * SingleThreadEventExecutor. So use it was care!</strong>
     */
    public int pendingTasks()
    {
        return taskQueue.size();
    }

    /**
     * Add a task to the task queue, or throws a {@link RejectedExecutionException} if this instance was shutdown
     * before.
     */
    private void addTask(Runnable task)
    {
        if (task == null)
        {
            throw new NullPointerException("task");
        }
        taskQueue.add(task);
    }

    @Override
    public boolean inEventLoop(Thread thread)
    {
        return thread == this.thread;
    }

    @Override
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
    {
        return terminationFuture();
    }

    @Override
    public Future<?> terminationFuture()
    {
        return terminationFuture;
    }

    @Override
    @Deprecated
    public void shutdown()
    {
        throw new UnsupportedOperationException();
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
    public boolean awaitTermination(long timeout, TimeUnit unit)
    {
        return false;
    }

    /**
     * Waits until the worker thread of this executor has no tasks left in its task queue and terminates itself.
     * Because a new worker thread will be started again when a new task is submitted, this operation is only useful
     * when you want to ensure that the worker thread is terminated <strong>after</strong> your application is shut
     * down and there's no chance of submitting a new task afterwards.
     *
     * @return {@code true} if and only if the worker thread has been terminated
     */
    public boolean awaitInactivity(long timeout, TimeUnit unit) throws InterruptedException
    {
        if (unit == null)
        {
            throw new NullPointerException("unit");
        }

        final Thread thread = this.thread;
        if (thread == null)
        {
            throw new IllegalStateException("thread was not started");
        }
        thread.join(unit.toMillis(timeout));
        return !thread.isAlive();
    }

    @Override
    public void execute(Runnable task)
    {
        if (task == null)
        {
            throw new NullPointerException("task");
        }

        addTask(task);
        if (!inEventLoop())
        {
            startThread();
        }
    }

    private void startThread()
    {
        if (started.compareAndSet(false, true))
        {
            Thread t = threadFactory.newThread(taskRunner);
            // Set the thread before starting it as otherwise inEventLoop() may return false and so produce
            // an assert error.
            // See https://github.com/netty/netty/issues/4357
            thread = t;
            t.start();
        }
    }

    final class TaskRunner implements Runnable
    {
        @Override
        public void run()
        {
            for (; ; )
            {
                Runnable task = takeTask();
                if (task != null)
                {
                    try
                    {
                        task.run();
                    } catch (Throwable t)
                    {
//                        LOG.warning(Module.COMMON, t, "Unexpected exception from the global event executor: ");
                    }

                    if (task != quietPeriodTask)
                    {
                        continue;
                    }
                }

                Queue<ScheduledFutureTask<?>> scheduledTaskQueue = GlobalEventExecutor.this.scheduledTaskQueue;
                // Terminate if there is no task in the queue (except the noop task).
                if (taskQueue.isEmpty() && (scheduledTaskQueue == null || scheduledTaskQueue.size() == 1))
                {
                    // Mark the current thread as stopped.
                    // The following CAS must always success and must be uncontended,
                    // because only one thread should be running at the same time.
                    boolean stopped = started.compareAndSet(true, false);
                    assert stopped;

                    // Check if there are pending entries added by execute() or schedule*() while we do CAS above.
                    if (taskQueue.isEmpty() && (scheduledTaskQueue == null || scheduledTaskQueue.size() == 1))
                    {
                        // A) No new task was added and thus there's nothing to handle
                        //    -> safe to terminate because there's nothing left to do
                        // B) A new thread started and handled all the new tasks.
                        //    -> safe to terminate the new thread will take care the rest
                        break;
                    }

                    // There are pending tasks added again.
                    if (!started.compareAndSet(false, true))
                    {
                        // startThread() started a new thread and set 'started' to true.
                        // -> terminate this thread so that the new thread reads from taskQueue exclusively.
                        break;
                    }

                    // New tasks were added, but this worker was faster to set 'started' to true.
                    // i.e. a new worker thread was not started by startThread().
                    // -> keep this thread alive to handle the newly added entries.
                }
            }
        }
    }

    @Override
    public int executorCount()
    {
        return 1;
    }
}

