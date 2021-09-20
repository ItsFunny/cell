package com.cell.executor.impl;

import com.cell.executor.IPrometheusExecutor;
import com.cell.hooks.IChainExecutor;
import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 08:02
 */
public class PrometheusExecutor implements IPrometheusExecutor
{

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor executor)
    {
        return executor.execute(context);
    }
}
