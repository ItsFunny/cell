package com.cell.schedual;

import com.cell.base.IScheduleCounter;
import com.cell.log.LOG;
import com.cell.models.Module;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 04:14
 */
public class SchedualCaculateErrorCount implements IScheduleCounter
{
    public static final int MIN_TOTAL_COUNT = 100;
    private AtomicInteger counter = new AtomicInteger();
    private AtomicInteger total = new AtomicInteger();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final int limit;
    private final long statInterval;
    private final double errorRateLimit;
    private String template;



    public SchedualCaculateErrorCount(int limit, long counterInterval, double errorRateLimit, String template)
    {
        this.limit = limit;
        this.statInterval = counterInterval;
        this.errorRateLimit = errorRateLimit;
        this.template = template;
    }

    @Override
    public void start()
    {
        executorService.scheduleAtFixedRate(() ->
        {
            int count = counter.get();
            int t = total.get();
            if (t != 0)
            {
                t = t > MIN_TOTAL_COUNT ? t : MIN_TOTAL_COUNT;
                double errorRate = Double.valueOf(count) / Double.valueOf(t);
                if (count >= limit || errorRate >= errorRateLimit)
                {
                    LOG.erroring(Module.HTTP_GATEWAY, "{}, 统计间隔 = {} ms, limit = {}, errorRateLimit = {}, total = {}, errorCount = {}, errorRate = {}", template,
                            statInterval, limit, errorRateLimit, total.get(), count, errorRate);
                }
            }
            reset();
        }, 0, statInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop()
    {
        executorService.shutdown();
    }

    @Override
    public int incAndGet()
    {
        return this.incAndGet(1);
    }

    @Override
    public int incTotal()
    {
        return total.incrementAndGet();
    }

    @Override
    public int incAndGet(int count)
    {
        return this.counter.addAndGet(count);
    }

    @Override
    public void reset()
    {
        counter.set(0);
        total.set(0);
    }
}
