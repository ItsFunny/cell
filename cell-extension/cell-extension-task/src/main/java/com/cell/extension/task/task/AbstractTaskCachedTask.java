package com.cell.extension.task.task;

import com.cell.base.common.utils.RandomUtils;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTaskCachedTask implements  ICacheableTask
{
    protected ITaskCache cache;
    protected long initialDelay;
    protected long period;
    protected TimeUnit unit;
    protected volatile boolean needClearBak = false;
    protected String taskName;
    public static final int DEFAULT_THREAD_COUNT = 1;
    private ScheduledExecutorService scheduledExecutorService;
    public static final String uniqueId = RandomUtils.randomString(128);

    @Override
    public void init(ITaskCache cache, long initialDelay, long period, TimeUnit unit)
    {
        this.cache=cache;
        this.initialDelay=initialDelay;
        this.period=period;
        this.unit=unit;
    }

    @Override
    public void addCache(String key, String value)
    {

    }

    @Override
    public String getOne(String key)
    {
        return null;
    }

    @Override
    public List<String> getAll(String key)
    {
        return null;
    }

    @Override
    public void addCache(String value)
    {

    }

    @Override
    public String getOne()
    {
        return null;
    }

    @Override
    public List<String> getAll()
    {
        return null;
    }

    @Override
    public ScheduledFuture<?> start(String taskName)
    {
        return null;
    }

    @Override
    public void close()
    {

    }

    @Override
    public void clearBak()
    {

    }
}
