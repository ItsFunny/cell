package com.cell.bee.event.simple;

import com.cell.base.core.events.IEvent;
import com.cell.executor.ChainExecutorFactory;
import com.cell.executor.IListChainExecutor;
import com.cell.manager.AbstractReflectManager;
import com.google.common.eventbus.Subscribe;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 09:52
 */
public abstract class AbstractSimpleEventCenter extends AbstractReflectManager<ISimpleEventHook, ISimpleChainEvent, IEvent>
{
    public void registerEventHook(ISimpleEventHook h)
    {
        this.pipeline.addFirst(h.getClass().getName(), h);
    }

    public AbstractSimpleEventCenter()
    {

    }

    @Subscribe
    public void hookWithReturn(IEvent event)
    {
        Disposable dos = this.hook(event).subscribe();
        dos.dispose();
    }

    protected abstract void afterInvoke();


    public Mono<Void> hook(IEvent event)
    {
        return this.pipeline.chainExecutor().execute(event);
    }

    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return () -> new SimpleHookChainExecutor();
    }

}
