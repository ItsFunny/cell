package com.cell.executor.abs;

import com.cell.context.IMetricsContext;
import com.cell.executor.IMetricsExecutor;
import com.cell.hooks.IChainExecutor;
import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 10:16
 */
public abstract class AbstractMetricsExecutor implements IMetricsExecutor
{

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor executor)
    {
        return this.update((IMetricsContext) context, executor);
    }

    protected abstract Mono<Void> update(IMetricsContext context, IChainExecutor executor);
}
