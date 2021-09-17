package com.cell.center;

import com.cell.events.IEvent;
import com.cell.hooks.*;
import com.cell.protocol.IEventContext;
import com.cell.services.ChainExecutorFactory;
import com.cell.services.impl.BaseMutableChainExecutor;
import com.cell.services.impl.DefaultHookMutableChainExecutor;
import com.cell.utils.CollectionUtils;
import com.google.common.eventbus.Subscribe;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 18:44
 */
public abstract class AbstractEventCenter extends AbstractReflectManager<IEventHook, IChainHook>
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
//    @Override
//    protected void onInvokeInterestNodes(Collection<Object> nodes)
//    {
//        if (CollectionUtils.isEmpty(nodes))
//        {
//            return;
//        }
//        for (Object node : nodes)
//        {
//            if (!(node instanceof IEventHook))
//            {
//                continue;
//            }
//            this.pipeline.addFirst(node.getClass().getName(), (IEventHook) node);
//        }
//        this.afterInvoke();
//    }


    class DefaultEventWrapper implements IEventContext
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
