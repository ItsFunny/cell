package com.cell.manager.nodes;

import com.cell.annotations.ActiveMethod;
import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ManagerNode;
import com.cell.center.AbstractHookCenter;
import com.cell.constants.GatewayConstants;
import com.cell.context.INodeContext;
import com.cell.context.MetricsContext;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.hooks.abs.AbstractHook;
import com.cell.manager.MetricsManager;
import com.cell.prometheus.HistogramStator;
import com.cell.protocol.IContext;
import com.cell.services.IStatContextService;
import com.cell.wrapper.ServerMetaInfoWrapper;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 14:38
 */
@ManagerNode(group = MetricsManager.gatewayMetricsManager, name = "gateway_metrics1")
public class MetricsManagerNode
{
    @AutoPlugin
    private HistogramStator exceedDelayThresoldCount;

    @AutoPlugin
    private IStatContextService statContextService;

    @ActiveMethod(id = MetricsManager.postFilterHook, description = "")
    public IReactorExecutor recordRequest()
    {
        return (context, executor) ->
        {
            MetricsContext metricsContext = (MetricsContext) context;
            ServerWebExchange exchange = metricsContext.getExchange();
            ServerMetaInfoWrapper wrapper = exchange.getAttribute(GatewayConstants.attributeCmdInfo);
            return executor.execute(context)
                    .then(this.recordRequest(wrapper, metricsContext.getStartTime(), exchange))
                    .onErrorResume(e -> this.recordRequest(wrapper, metricsContext.getStartTime(), exchange));
        };
    }


    private Mono<Void> recordRequest(ServerMetaInfoWrapper wrapper, long startTime, ServerWebExchange exchange)
    {
        return Mono.fromRunnable(() ->
        {
            long cost = System.currentTimeMillis() - startTime;
            INodeContext ctx = this.statContextService.getNodeContext();
            this.exceedDelayThresoldCount.labels(ctx.getNodeName(), ctx.getCluster(), wrapper.getMetaInfo().getModule(), wrapper.getUri().getPath(), wrapper.getMethod()).observe(cost);
        });
    }
}
