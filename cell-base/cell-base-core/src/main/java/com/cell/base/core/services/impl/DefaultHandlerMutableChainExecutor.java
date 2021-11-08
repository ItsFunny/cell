package com.cell.base.core.services.impl;

import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.services.IHandlerSuit;
import com.cell.executor.IChainExecutor;
import com.cell.executor.IReactorExecutor;
import com.cell.executor.impl.BaseMutableChainExecutor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-18 22:15
 */
public class DefaultHandlerMutableChainExecutor extends BaseMutableChainExecutor<IContext> implements IChainHandler
{

    public DefaultHandlerMutableChainExecutor()
    {
    }

    public DefaultHandlerMutableChainExecutor(List<? extends IReactorExecutor<IContext>> iReactorExecutors)
    {
        super(iReactorExecutors);
    }

    public DefaultHandlerMutableChainExecutor(List<? extends IReactorExecutor<IContext>> iReactorExecutors, int index)
    {
        super(iReactorExecutors, index);
    }

    @Override
    public Mono<Void> exceptionCaught(IHandlerSuit suit, Throwable e)
    {
        return Mono.defer(() ->
        {
            if (this.index < this.executors.size())
            {
                IHandler h = (IHandler) this.executors.get(this.index);
                DefaultHandlerMutableChainExecutor hh = new DefaultHandlerMutableChainExecutor(this.executors, this.index + 1);
                return h.exceptionCaught(suit, e, hh);
            } else
            {
                return Mono.empty();
            }
        });
    }

    @Override
    protected IChainExecutor childNewExecutor(List<? extends IReactorExecutor<IContext>> iReactorExecutors, int index)
    {
        return new DefaultHandlerMutableChainExecutor(executors, index);
    }
}
