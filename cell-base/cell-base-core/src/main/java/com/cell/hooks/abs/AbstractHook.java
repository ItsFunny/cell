package com.cell.hooks.abs;

import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IHook;
import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 11:00
 */
public abstract  class AbstractHook implements IHook
{
    @Override
    public Mono<Void> execute(IContext context, IChainExecutor executor)
    {
        return this.hook(context,executor);
    }
    protected abstract  Mono<Void> hook(IContext context,IChainExecutor executor);
}
