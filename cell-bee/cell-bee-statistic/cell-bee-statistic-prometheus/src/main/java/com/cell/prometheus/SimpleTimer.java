package com.cell.prometheus;

import io.prometheus.client.Collector;

/**
 * @author Charlie
 * @When
 * @Description 抄于prometheus
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:06
 */
public class SimpleTimer
{
    private final long start;
    public static TimeProvider defaultTimeProvider = new TimeProvider();
    private final TimeProvider timeProvider;

    static class TimeProvider
    {
        long nanoTime()
        {
            return System.nanoTime();
        }
    }

    // Visible for testing.
    SimpleTimer(TimeProvider timeProvider)
    {
        this.timeProvider = timeProvider;
        start = timeProvider.nanoTime();
    }

    public SimpleTimer()
    {
        this(defaultTimeProvider);
    }

    /**
     * @return Measured duration in seconds since {@link SimpleTimer} was constructed.
     */
    public double elapsedSeconds()
    {
        return elapsedSecondsFromNanos(start, timeProvider.nanoTime());
    }

    public static double elapsedSecondsFromNanos(long startNanos, long endNanos)
    {
        return (endNanos - startNanos) / Collector.NANOSECONDS_PER_SECOND;
    }
}
