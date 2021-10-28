package com.cell.hook;

import com.cell.context.IRPCContext;
import com.cell.hooks.IChainHook;
import com.cell.hooks.abs.AbstractCommandHook;
import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:56
 */
public abstract class AbstractRPCHook extends AbstractCommandHook
{
    @Override
    protected Mono<Void> onHook(IContext ctx, IChainHook hook)
    {
        return this.doHook((IRPCContext) ctx, hook);
    }

    protected abstract Mono<Void> doHook(IRPCContext context, IChainHook hook);
}
