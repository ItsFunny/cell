package com.cell.plugin.develop.context.event;


import com.cell.base.common.events.IEvent;
import com.cell.sdk.log.LOG;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 21:14
 */
@Data
public class JobCenter
{
    // FIXME ,CUSTOMIZE
    private static final JobCenter instance = new JobCenter();

    private DefaultEventLoopGroup eventExecutors = new DefaultEventLoopGroup(256);
    // TODO
    private final EventBus bus = new AsyncEventBus("jobCenter", this.eventExecutors);
    private final EventBus syncBus = new EventBus();


    public static JobCenter getInstance()
    {
        return instance;
    }

    public void addJob(IEvent job)
    {
        LOG.info("add job async:{}", job);
        this.bus.post(job);
    }

    public void addSyncJob(IEvent job)
    {
        this.syncBus.post(job);
    }

    public Promise addPromiseJob(IEvent job)
    {
        PromiseWrapper<Object> wp = new PromiseWrapper<>(job);
        this.bus.post(wp);
        return wp.getPromise();
    }

    public Promise addSyncPromiseJob(IEvent job)
    {
        PromiseWrapper<Object> wp = new PromiseWrapper<>(job);
        this.syncBus.post(wp);
        return wp.getPromise();
    }

    public void registerSubscriber(JobWorker o)
    {
        PromiseProxyWorker promiseProxyWorker = new PromiseProxyWorker(o);
        this.bus.register(promiseProxyWorker);
        this.syncBus.register(promiseProxyWorker);
        this.bus.register(o);
        this.syncBus.register(o);
    }

    public static void main(String[] args)
    {

    }
}
