package com.cell.extension.task.event;


import com.cell.base.common.events.IEvent;
import com.cell.base.common.models.Module;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.extension.task.wrapper.PromiseWrapper;
import com.cell.node.core.context.INodeContext;
import com.cell.sdk.log.LOG;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
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
public class CellJobCenter
{
    private static final CellJobCenter instance = new CellJobCenter();

    private EventLoopGroup eventExecutors;
    private EventBus bus;
    private EventBus syncBus;

    public void seal(INodeContext nodeContext)
    {
        this.eventExecutors = nodeContext.getEventLoopGroup();
        this.bus = new AsyncEventBus("jobCenter", this.eventExecutors);
        this.syncBus = new EventBus();
    }

    public static CellJobCenter getInstance()
    {
        return instance;
    }

    public void addJob(IEvent job)
    {
        LOG.info(Module.WORKER, "add job async:{}", job);
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

    public void addAsyncJob(IEvent job)
    {
        this.bus.post(job);
    }

    public Promise addSyncPromiseJob(IEvent job)
    {
        PromiseWrapper<Object> wp = new PromiseWrapper<>(job);
        this.syncBus.post(wp);
        return wp.getPromise();
    }

    public void registerSubscriber(Object o)
    {
        this.bus.register(o);
        this.syncBus.register(o);
    }
}
