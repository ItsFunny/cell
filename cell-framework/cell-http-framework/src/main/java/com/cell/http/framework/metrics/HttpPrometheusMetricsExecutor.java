package com.cell.http.framework.metrics;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.protocol.IContext;
import com.cell.monitor.base.executor.MetricsManager;
import com.cell.monitor.base.executor.abs.AbstractMetricsExecutor;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.monitor.base.executor.IMetricsExecutor;
import com.cell.plugin.pipeline.annotation.ActiveMethod;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 17:52
 */
@ManagerNode(group = MetricsManager.metricsManager, name = "HttpPrometheusMetricsExecutor")
public class HttpPrometheusMetricsExecutor extends AbstractMetricsExecutor
{
    @ActiveMethod(id = IMetricsExecutor.recordRequest)
    @Override
    public IReactorExecutor<IContext> recordRequest()
    {
        return (ctx, c) ->
        {
            return c.execute(ctx);
        };
    }

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor<IContext> executor)
    {
        return executor.execute(context);
    }
}
