package com.cell.component.download.common.provider;

import com.cell.base.core.protocol.IContext;
import com.cell.component.download.common.provider.event.ProxyWrapperEvent;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

public abstract  class AbstractProxyHandler implements IReactorExecutor<IContext>
{
    @Override
    public boolean predict(IContext context)
    {
        return context instanceof ProxyWrapperEvent;
    }

    @Override
    public Mono<Void> execute(IContext context, IChainExecutor<IContext> executor)
    {
        this.doExecute((ProxyWrapperEvent) context);
        return executor.execute(context);
    }
    protected abstract void doExecute(ProxyWrapperEvent event);
}
