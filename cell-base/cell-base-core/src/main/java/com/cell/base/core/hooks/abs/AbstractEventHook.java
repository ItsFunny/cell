package com.cell.base.core.hooks.abs;

import com.cell.base.common.events.IEvent;
import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.hooks.IEventHook;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.protocol.IEventContext;
import com.cell.executor.IChainExecutor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail 用于捕获event
 * @Attention:
 * @Date 创建时间：2021-09-19 11:30
 */
public abstract class AbstractEventHook extends AbstractHook implements IEventHook
{

    @Override
    public boolean predict(IContext ctx)
    {
        IEventContext f = (IEventContext) ctx;
        return this.onPredict(f.getEvent());
    }

    @Override
    protected Mono<Void> hook(IContext context, IChainExecutor executor)
    {
        return this.onEventHook((IEventContext) context, (IChainHook) executor);
    }

    protected abstract Mono<Void> onEventHook(IEventContext context, IChainHook hook);

    protected abstract boolean onPredict(IEvent event);
}
