package com.cell.base.core.protocol;

import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

public abstract class AbstractReactorExecutor implements IReactorExecutor<IContext>
{

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor<IContext> executor)
    {
        this.doExecute(context);
        return executor.execute(context);
    }

    protected abstract void doExecute(IContext context);
}
