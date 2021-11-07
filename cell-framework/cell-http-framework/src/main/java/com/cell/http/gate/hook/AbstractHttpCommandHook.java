package com.cell.http.gate.hook;


import com.cell.context.IHttpCommandContext;
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
 * @Date 创建时间：2021-08-28 11:06
 */
public abstract class AbstractHttpCommandHook extends AbstractCommandHook implements IHttpCommandHook
{
    @Override
    protected Mono<Void> onHook(IContext ctx, IChainHook hook)
    {
        return this.doHook((IHttpCommandContext) ctx, hook);
    }

    protected abstract Mono<Void> doHook(IHttpCommandContext ctx, IChainHook hook);
}
