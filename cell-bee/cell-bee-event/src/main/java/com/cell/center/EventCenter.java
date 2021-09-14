package com.cell.center;

import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.manager.IReflectManager;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:30
 */
public class EventCenter implements IHookChain<IEvent>, IReflectManager
{
    private static final EventCenter instance = new EventCenter(Arrays.asList());
    public static EventCenter getInstance(){
        return instance;
    }
    private List<IEventHook<IEvent>> hooks;
    private final int index;

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

    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {

    }

    @Override
    public String name()
    {
        return null;
    }

    @Override
    public boolean override()
    {
        return false;
    }
}
