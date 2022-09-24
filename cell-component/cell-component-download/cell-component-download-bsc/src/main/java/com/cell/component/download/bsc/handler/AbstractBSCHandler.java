package com.cell.component.download.bsc.handler;

import com.cell.base.core.protocol.AbstractReactorExecutor;
import com.cell.base.core.protocol.IContext;
import com.cell.component.download.bsc.wrapper.BSCEventWrapper;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

public abstract class AbstractBSCHandler implements IReactorExecutor<IContext>
{

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor<IContext> executor)
    {
        this.doExecute(context);
        return executor.execute(context);
    }

    protected abstract void doExecute(IContext wrapper);
}

