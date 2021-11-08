package com.cell.base.core.hooks.abs;

import com.cell.base.core.hooks.IHook;
import com.cell.base.core.protocol.IContext;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 11:00
 */
public abstract class AbstractHook implements IHook
{
    @Override
    public Mono<Void> execute(IContext context, IChainExecutor executor)
    {
        return this.hook(context, executor);
    }

    protected abstract Mono<Void> hook(IContext context, IChainExecutor executor);
}
