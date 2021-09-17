package com.cell.hook;


import com.cell.context.IHttpCommandContext;
import com.cell.context.IHttpHandlerSuit;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IChainHook;
import com.cell.hooks.abs.AbstractHook;
import com.cell.log.LOG;
import com.cell.models.Module;
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
public abstract class AbstractHttpCommandHook extends AbstractHook implements IHttpCommandHook
{
    @Override
    protected Mono<Void> hook(IContext context, IChainExecutor executor)
    {
        return this.onHook((IHttpCommandContext) context, (IChainHook) executor);
    }

    protected abstract Mono<Void> onHook(IHttpCommandContext ctx, IChainHook hook);

    @Override
    public void onExceptionCaught(Throwable e)
    {
        LOG.error(Module.HTTP_FRAMEWORK, e, "执行cmd发生错误");
    }
}
