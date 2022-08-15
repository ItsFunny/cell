package com.cell.component.http.filter.manager.executors;

import com.cell.base.core.protocol.IContext;
import com.cell.node.core.context.CellContext;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

public abstract class AbstractFilterExecutor implements IReactorExecutor<IContext>
{

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor<IContext> executor)
    {
        this.onExecute((CellContext) context);
        return executor.execute(context);
    }

    protected abstract void onExecute(CellContext context);
}
