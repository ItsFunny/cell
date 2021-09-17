package com.cell.hooks.abs;

import com.cell.events.IEvent;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IChainHook;
import com.cell.hooks.IEventHook;
import com.cell.protocol.IContext;
import com.cell.protocol.IEventContext;
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
        return this.onEventHook( (IEventContext) context, (IChainHook) executor);
    }

    protected abstract  Mono<Void>onEventHook(IEventContext context, IChainHook hook);
    protected abstract boolean onPredict(IEvent event);
}
