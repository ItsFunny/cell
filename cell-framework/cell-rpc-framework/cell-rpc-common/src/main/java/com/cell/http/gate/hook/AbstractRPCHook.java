package com.cell.http.gate.hook;

import com.cell.base.core.hooks.abs.AbstractCommandHook;
import com.cell.context.IRPCContext;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.protocol.IContext;
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

    protected Mono<Void> doHook(IRPCContext context, IChainHook hook)
    {
        context.getReactor().execute(context);
        return hook.execute(context).doOnError(this::onExceptionCaught).then(Mono.fromRunnable(() ->
        {
        }));
    }


}
