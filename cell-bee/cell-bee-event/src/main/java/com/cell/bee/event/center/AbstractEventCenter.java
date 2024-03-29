package com.cell.bee.event.center;

import com.cell.base.common.events.IEvent;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.hooks.IEventHook;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.protocol.IEventContext;
import com.cell.base.core.services.impl.DefaultHookMutableChainExecutor;
import com.cell.plugin.pipeline.executor.ChainExecutorFactory;
import com.cell.plugin.pipeline.executor.IListChainExecutor;
import com.cell.plugin.pipeline.manager.AbstractReflectManager;
import com.google.common.eventbus.Subscribe;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 18:44
 */
public abstract class AbstractEventCenter extends AbstractReflectManager<IEventHook, IChainHook, IContext>
{
    public static final String GROUP_EVENT_CENTER = "GROUP_EVENT_CENTER";

    public void registerEventHook(IEventHook h)
    {
        this.pipeline.addFirst(h.getClass().getName(), h);
    }

    public AbstractEventCenter()
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
        return this.pipeline.chainExecutor().execute(new DefaultEventWrapper(event));
    }


    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return () -> new DefaultHookMutableChainExecutor();
    }

    public class DefaultEventWrapper implements IEventContext
    {
        public DefaultEventWrapper(IEvent event)
        {
            this.event = event;
        }

        private IEvent event;

        @Override
        public IEvent getEvent()
        {
            return this.event;
        }

        @Override
        public void discard()
        {

        }
    }
}
