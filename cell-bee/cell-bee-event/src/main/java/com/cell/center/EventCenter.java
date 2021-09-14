package com.cell.center;

import com.cell.annotations.Manager;
import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.manager.AbstractReflectManager;
import com.cell.manager.IReflectManager;
import com.cell.utils.CollectionUtils;
import com.google.common.eventbus.Subscribe;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description internal使用
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:30
 */
@Manager(name = EventCenter.GROUP_EVENT_CENTER)
public class EventCenter extends AbstractReflectManager implements IHookChain<IEvent>
{
    public static final String GROUP_EVENT_CENTER = "GROUP_EVENT_CENTER";
    private static final EventCenter instance = new EventCenter(new ArrayList<>());

    public static EventCenter getInstance()
    {
        return instance;
    }

    private List<IEventHook<IEvent>> hooks;
    private final int index;

    public void registerEventHook(IEventHook<IEvent> h)
    {
        this.hooks.add(h);
    }

    EventCenter(List<IEventHook<IEvent>> hooks)
    {
        this.hooks = hooks;
        this.index = 0;
    }

    private EventCenter(EventCenter parent, int index)
    {
        this.hooks = parent.hooks;
        this.index = index;
    }


    @Override
    public Mono<Void> hook(IEvent event)
    {
        return Mono.defer(() ->
        {
            if (this.index < this.hooks.size())
            {
                IEventHook<IEvent> h = this.hooks.get(this.index);
                EventCenter hh = new EventCenter(this,
                        this.index + 1);
                return h.hook(event, hh);
            } else
            {
                return Mono.empty();
            }
        });
    }

    @Subscribe
    public void hookWithReturn(IEvent event)
    {
        Disposable subscribe = this.hook(event).subscribe();
        subscribe.dispose();
    }

    @Override
    protected void onInvokeInterestNodes(Collection<Object> nodes)
    {
        if (CollectionUtils.isEmpty(nodes))
        {
            return;
        }
        for (Object node : nodes)
        {
            if (!(node instanceof IEventHook))
            {
                continue;
            }
            this.hooks.add((IEventHook<IEvent>) node);
        }
        JobCenter.getInstance().registerSubscriber(this);
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
