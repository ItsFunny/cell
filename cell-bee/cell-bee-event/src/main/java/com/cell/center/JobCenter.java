package com.cell.center;

import com.cell.events.IEvent;
import com.cell.utils.ReflectUtil;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 21:14
 */
public class JobCenter
{
    // FIXME ,CUSTOMIZE
    private static final JobCenter instance = new JobCenter();
    private final EventBus bus = new EventBus();

    public static JobCenter getInstance(){
        return instance;
    }
    public void addJob(IEvent job)
    {
        this.bus.post(job);
    }

    public void registerSubscriber(Object o)
    {
        this.bus.register(o);
    }

    private void run()
    {


    }
}
