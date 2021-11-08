package com.cell.base.core.hooks.abs;


import com.cell.base.common.models.Module;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.hooks.ICommandHook;
import com.cell.sdk.log.LOG;
import com.cell.base.core.protocol.IContext;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:06
 */
public abstract class AbstractCommandHook extends AbstractHook implements ICommandHook
{
    @Override
    protected Mono<Void> hook(IContext context, IChainExecutor executor)
    {
        return this.onHook(context, (IChainHook) executor);
    }

    protected abstract Mono<Void> onHook(IContext ctx, IChainHook hook);

    @Override
    public void onExceptionCaught(Throwable e)
    {
        LOG.error(Module.HOOK, e, "执行cmd发生错误");
    }
}
