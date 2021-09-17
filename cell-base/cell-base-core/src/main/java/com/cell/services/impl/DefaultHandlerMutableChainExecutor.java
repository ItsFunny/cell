package com.cell.services.impl;

import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IHook;
import com.cell.hooks.IReactorExecutor;
import com.cell.protocol.IContext;
import com.cell.services.IHandlerSuit;
import lombok.Data;
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
public class DefaultHandlerMutableChainExecutor extends BaseMutableChainExecutor<IHandler> implements IChainHandler
{
    public DefaultHandlerMutableChainExecutor(){}
    public DefaultHandlerMutableChainExecutor(List<IHandler> executors)
    {
        super(executors);
    }

    public DefaultHandlerMutableChainExecutor(List<IHandler> executors, int index)
    {
        super(executors, index);
    }

    @Override
    protected IChainExecutor childNewExecutor(List<IHandler> executors, int index)
    {
        return new DefaultHandlerMutableChainExecutor(executors, index);
    }


    @Override
    public Mono<Void> exceptionCaught(IHandlerSuit suit, Throwable e)
    {
        return Mono.defer(() ->
        {
            if (this.index < this.executors.size())
            {
                IHandler h = this.executors.get(this.index);
                DefaultHandlerMutableChainExecutor hh = new DefaultHandlerMutableChainExecutor(this.executors, this.index + 1);
                return h.exceptionCaught(suit, e, hh);
            } else
            {
                return Mono.empty();
            }
        });
    }

}
