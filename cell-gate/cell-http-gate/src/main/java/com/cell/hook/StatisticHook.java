package com.cell.hook;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ManagerNode;
import com.cell.center.EventCenter;
import com.cell.event.StasticEvent;
import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.prometheus.HistogramStator;
import com.cell.services.IStatContextService;
import com.cell.utils.MetaDataUtils;
import com.sun.xml.internal.ws.util.MetadataUtil;
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
    @AutoPlugin
    private HistogramStator exceedDelayThresoldCount;
    @AutoPlugin
    private IStatContextService statContextService;

    @Override
    public Mono<Void> hook(IEvent t, IHookChain hook)
    {
        StasticEvent event = (StasticEvent) t;
        long cost = event.getEndTime() - event.getStartTIme();
        exceedDelayThresoldCount.labels(MetaDataUtils.getHttpLabels(event.getMethod(), event.getUri(), statContextService)).observe(cost);
        return hook.hook(t);
    }

    @Override
    public boolean predict(IEvent t)
    {
        return t instanceof StasticEvent;
    }
}
