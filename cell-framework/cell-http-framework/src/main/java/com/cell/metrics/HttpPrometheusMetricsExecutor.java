package com.cell.metrics;

import com.cell.annotations.ActiveMethod;
import com.cell.annotations.ManagerNode;
import com.cell.context.IMetricsContext;
import com.cell.executor.IMetricsExecutor;
import com.cell.executor.MetricsManager;
import com.cell.executor.abs.AbstractMetricsExecutor;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.protocol.IContext;
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
    public IReactorExecutor recordRequest()
    {
        return (ctx, c) ->
        {
            return c.execute(ctx);
        };
    }

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor executor)
    {
        return executor.execute(context);
    }
}
