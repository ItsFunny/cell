package com.cell.executor;

import com.cell.context.IMetricsContext;
import com.cell.executor.abs.AbstractMetricsExecutor;
import com.cell.hooks.IChainExecutor;
import com.cell.protocol.ICommand;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 10:44
 */
public class PrometheusMetricsExecutor extends AbstractMetricsExecutor
{
    @Override
    protected Mono<Void> update(IMetricsContext context, IChainExecutor executor)
    {
        return null;
    }

    @Override
    public void recordRequest(ICommand cmd)
    {

    }
}
