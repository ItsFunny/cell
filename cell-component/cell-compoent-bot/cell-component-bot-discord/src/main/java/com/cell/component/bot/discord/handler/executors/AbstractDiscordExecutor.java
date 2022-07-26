package com.cell.component.bot.discord.handler.executors;

import com.cell.base.core.protocol.IContext;
import com.cell.component.bot.discord.handler.DiscordWrapper;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

public abstract class AbstractDiscordExecutor implements IReactorExecutor<IContext>
{
    @Override
    public boolean predict(IContext iContext)
    {
        DiscordWrapper eventWrapper = (DiscordWrapper) iContext;
        if (eventWrapper.isHandled())
        {
            return false;
        }
        return this.doPredict(eventWrapper);
    }

    protected abstract boolean doPredict(DiscordWrapper discordWrapper);

    @Override
    public Mono<Void> execute(IContext iContext, IChainExecutor<IContext> iChainExecutor)
    {
        DiscordWrapper wrapper = (DiscordWrapper) iContext;
        wrapper.setHandled(true);
        this.doExecute(wrapper);
        return iChainExecutor.execute(iContext);
    }

    protected abstract void doExecute(DiscordWrapper eventWrapper);
}
