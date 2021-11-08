package com.cell.hooks.abs;


import com.cell.base.common.models.Module;
import com.cell.executor.IChainExecutor;
import com.cell.hooks.IChainHook;
import com.cell.hooks.ICommandHook;
import com.cell.log.LOG;
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
