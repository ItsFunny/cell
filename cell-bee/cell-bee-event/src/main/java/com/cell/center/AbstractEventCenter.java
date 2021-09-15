package com.cell.center;

import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.manager.AbstractReflectManager;
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
public abstract class AbstractEventCenter extends AbstractReflectManager
{
    public static final String GROUP_EVENT_CENTER = "GROUP_EVENT_CENTER";
    private List<IEventHook> hooks = new ArrayList<>();

    public void registerEventHook(IEventHook h)
    {
        this.hooks.add(h);
    }

    public AbstractEventCenter()
    {

    }


    private class DefaultEventHook implements IHookChain<IEvent>
    {
        DefaultEventHook(List<IEventHook> hooks)
        {
            this.hooks = hooks;
            this.index = 0;
        }

        private DefaultEventHook(DefaultEventHook parent, int index)
        {
            this.hooks = parent.hooks;
            this.index = index;
        }

        private List<IEventHook> hooks;
        private int index;

        @Override
        public Mono<Void> hook(IEvent event)
        {
            return Mono.defer(() ->
            {
                boolean find = false;
                if (this.index < this.hooks.size())
                {
                    IEventHook h = null;
                    for (; this.index < this.hooks.size(); this.index++)
                    {
                        h = this.hooks.get(this.index);
                        if (h.predict(event))
                        {
                            find = true;
                            break;
                        }
                    }
                    if (!find) return Mono.empty();
                    DefaultEventHook hh = new DefaultEventHook(this,
                            this.index + 1);
                    return h.hook(event, hh);
                } else
                {
                    return Mono.empty();
                }
            });
        }
    }


    @Subscribe
    public void hookWithReturn(IEvent event)
    {
        Disposable subscribe = new DefaultEventHook(this.hooks).hook(event).subscribe();
        subscribe.dispose();
    }

    @Override
    protected void onInvokeInterestNodes(Collection<Object> nodes)
    {
        JobCenter.getInstance().registerSubscriber(this);
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
            this.hooks.add((IEventHook) node);
        }
    }


}
