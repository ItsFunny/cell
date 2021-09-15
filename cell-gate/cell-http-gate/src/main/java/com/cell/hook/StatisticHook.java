package com.cell.hook;

import com.cell.annotations.ManagerNode;
import com.cell.center.EventCenter;
import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.model.StasticEvent;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 05:37
 */
@ManagerNode(group = EventCenter.GROUP_EVENT_CENTER, name = "STATISTIC_HOOK")
public class StatisticHook implements IEventHook
{
    @Override
    public Mono<Void> hook(IEvent t, IHookChain hook)
    {
        return hook.hook(t);
    }

    @Override
    public boolean predict(IEvent t)
    {
        return t instanceof StasticEvent;
    }
}
