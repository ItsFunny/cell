package com.cell.http.gate.manager.nodes;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.executor.IBaseReactorExecutor;
import com.cell.bee.statistic.prometheus.HistogramStator;
import com.cell.bee.statistic.prometheus.services.IStatContextService;
import com.cell.http.gate.constants.GatewayConstants;
import com.cell.http.gate.context.MetricsContext;
import com.cell.http.gate.manager.MetricsManager;
import com.cell.http.gate.wrapper.ServerMetaInfoWrapper;
import com.cell.node.core.context.INodeContext;
import com.cell.plugin.pipeline.annotation.ActiveMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    @Autowired
    private HistogramStator exceedDelayThresoldCount;

    @AutoPlugin
    private IStatContextService statContextService;

    @ActiveMethod(id = MetricsManager.postFilterHook, description = "")
    public IBaseReactorExecutor recordRequest()
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
            this.exceedDelayThresoldCount.labels(ctx.getNodeName(), ctx.getCluster(), wrapper.getModule(), wrapper.getUri().getPath(), wrapper.getMethod()).observe(cost);
        });
    }
}
