package com.cell.extension.task.task;


import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface ICacheableTask extends Runnable
{

    void init(ITaskCache cache, long initialDelay, long period, TimeUnit unit);

    void addCache(String key, String value);

    String getOne(String key);

    List<String> getAll(String key);

    void addCache(String value);

    String getOne();

    List<String> getAll();

    ScheduledFuture<?> start(String taskName);

    void close();

    void clearBak();
}