package com.cell.concurrent.base;

/**
 * Expose details for a {@link Thread}.
 */
public interface ThreadProperties {
    /**
     * @see {@link Thread#getState()}.
     */
    Thread.State state();

    /**
     * @see {@link Thread#getPriority()}.
     */
    int priority();

    /**
     * @see {@link Thread#isInterrupted()}.
     */
    boolean isInterrupted();

    /**
     * @see {@link Thread#isDaemon()} ()}.
     */
    boolean isDaemon();

    /**
     * @see {@link Thread#getName()} ()}.
     */
    String name();

    /**
     * @see {@link Thread#getId()}.
     */
    long id();

    /**
     * @see {@link Thread#getStackTrace()}.
     */
    StackTraceElement[] stackTrace();

    /**
     * @see {@link Thread#isAlive()}.
     */
    boolean isAlive();
}
