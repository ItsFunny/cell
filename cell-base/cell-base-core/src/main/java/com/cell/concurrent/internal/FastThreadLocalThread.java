package com.cell.concurrent.internal;

/**
 * A special {@link Thread} that provides fast access to {@link FastThreadLocal} variables.
 */
public class FastThreadLocalThread extends Thread
{

    private InternalThreadLocalMap threadLocalMap;

    public FastThreadLocalThread() { }

    public FastThreadLocalThread(Runnable target)
    {
        super(target);
    }

    public FastThreadLocalThread(ThreadGroup group, Runnable target)
    {
        super(group, target);
    }

    public FastThreadLocalThread(String name)
    {
        super(name);
    }

    public FastThreadLocalThread(ThreadGroup group, String name)
    {
        super(group, name);
    }

    public FastThreadLocalThread(Runnable target, String name)
    {
        super(target, name);
    }

    public FastThreadLocalThread(ThreadGroup group, Runnable target, String name)
    {
        super(group, target, name);
    }

    public FastThreadLocalThread(ThreadGroup group, Runnable target, String name, long stackSize)
    {
        super(group, target, name, stackSize);
    }

    /**
     * Returns the internal data structure that keeps the thread-local variables bound to this thread.
     * Note that this method is for internal use only, and thus is subject to change at any time.
     */
    public final InternalThreadLocalMap threadLocalMap()
    {
        return threadLocalMap;
    }

    /**
     * Sets the internal data structure that keeps the thread-local variables bound to this thread.
     * Note that this method is for internal use only, and thus is subject to change at any time.
     */
    public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap)
    {
        this.threadLocalMap = threadLocalMap;
    }
}
