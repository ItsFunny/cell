package com.cell.extension.task.util;

import com.cell.base.common.models.Module;
import com.cell.sdk.log.LOG;
import org.apache.logging.log4j.core.util.CronExpression;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.*;


public class ScheduleExecutorUtil
{
    private ScheduleExecutorUtil()
    {
    }

    public static ScheduleExecutorUtil getInstance()
    {
        return ScheduleExecutorUtilHolder.INSTANCE;
    }

    private static class ScheduleExecutorUtilHolder
    {
        private static final ScheduleExecutorUtil INSTANCE = new ScheduleExecutorUtil();
    }

    // 防止反序列化生成新的实例
    private Object readResolve()
    {
        return ScheduleExecutorUtilHolder.INSTANCE;
    }

    private static final int DEFAULT_THREAD_POOL_SIZE = 1;
    private ScheduledExecutorService scheduledExecutorService;
    private int preCorePoolSize;

    public synchronized void init(Integer corePoolSize)
    {
        if (preCorePoolSize != 0 && preCorePoolSize >= corePoolSize)
        {
            LOG.info(Module.TASK_CA, "ScheduledExecutorService has initialized, preCodePoolSize = %d, corePoolSize = %d", preCorePoolSize, corePoolSize);
            return;
        }
        scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize == null ? DEFAULT_THREAD_POOL_SIZE : corePoolSize);
        preCorePoolSize = corePoolSize;
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay, TimeUnit unit)
    {
        return scheduledExecutorService.schedule(callable, delay, unit);
    }

    public <V> ScheduledFuture<?> schedule(Runnable callable,
                                           long delay, TimeUnit unit)
    {
        return scheduledExecutorService.schedule(callable, delay, unit);
    }

    public void execute(Runnable task)
    {
        scheduledExecutorService.execute(task);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)
    {
        return scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public static long getNextExecuteTime(String cron) throws ParseException
    {
        CronExpression cronExpression = new CronExpression(cron);
        Date date = cronExpression.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        return date.getTime();
    }

    public static long getInitialDelay(String cron) throws ParseException
    {
        return getNextExecuteTime(cron) - System.currentTimeMillis();
    }

    public static void main(String[] args) throws InterruptedException
    {
        ScheduleExecutorUtil.getInstance().init(10);
        ScheduleExecutorUtil.getInstance().scheduleAtFixedRate(()->{
            System.out.println("123");
        },1,1,TimeUnit.SECONDS);
        ScheduleExecutorUtil.getInstance().schedule(() ->
        {
            System.out.println(123333);
        }, 1l, TimeUnit.SECONDS);
        Thread.sleep(10009999990L);
    }

}
