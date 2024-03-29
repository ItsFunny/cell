package com.cell.http.gate.hook;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.hooks.abs.AbstractEventHook;
import com.cell.bee.event.center.EventCenter;
import com.cell.bee.rate.gateway.sentinel.event.StasticEvent;
import com.cell.bee.statistic.prometheus.HistogramStator;
import com.cell.bee.statistic.prometheus.services.IStatContextService;
import com.cell.base.common.events.IEvent;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.hooks.IEventHook;
import com.cell.http.gate.utils.MetaDataUtils;
import com.cell.base.core.protocol.IEventContext;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StatisticHook extends AbstractEventHook implements IEventHook
{
    @Autowired
    private HistogramStator exceedDelayThresoldCount;
    @AutoPlugin
    private IStatContextService statContextService;


    @Override
    protected Mono<Void> onEventHook(IEventContext context, IChainHook hook)
    {
        StasticEvent event = (StasticEvent) context.getEvent();
        long cost = event.getEndTime() - event.getStartTIme();
        exceedDelayThresoldCount.labels(MetaDataUtils.getHttpLabels(event.getMethod(), event.getUri(), statContextService)).observe(cost);
        return hook.execute(context);
    }

    @Override
    protected boolean onPredict(IEvent event)
    {
        return event instanceof StasticEvent;
    }
}
