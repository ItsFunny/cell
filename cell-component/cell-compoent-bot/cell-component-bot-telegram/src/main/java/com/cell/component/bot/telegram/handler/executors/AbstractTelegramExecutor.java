package com.cell.component.bot.telegram.handler.executors;

import com.cell.base.core.protocol.IContext;
import com.cell.component.bot.telegram.handler.EventWrapper;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

public abstract class AbstractTelegramExecutor implements IReactorExecutor<IContext>
{
    @Override
    public boolean predict(IContext iContext)
    {
        EventWrapper eventWrapper = (EventWrapper) iContext;
        if (eventWrapper.isHandled())
        {
            return false;
        }
        return this.doPredict(eventWrapper);
    }

    protected abstract boolean doPredict(EventWrapper wrapper);

    @Override
    public Mono<Void> execute(IContext iContext, IChainExecutor<IContext> iChainExecutor)
    {
        EventWrapper wrapper = (EventWrapper) iContext;
        wrapper.setHandled(true);
        this.doExecute(wrapper);
        return iChainExecutor.execute(iContext);
    }

    protected abstract void doExecute(EventWrapper eventWrapper);
}
