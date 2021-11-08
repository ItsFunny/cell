package com.cell.base.core.handler.abs;

import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.services.IHandlerSuit;
import com.cell.executor.IChainExecutor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 10:58
 */
public abstract class AbstractHandler implements IHandler
{
    @Override
    public void init(IHandlerSuit ctx)
    {

    }

    @Override
    public void close(IHandlerSuit ctx)
    {

    }

    @Override
    public Mono<Void> exceptionCaught(IHandlerSuit suit, Throwable e, IChainHandler executor)
    {
        return executor.exceptionCaught(suit, e);
    }


    @Override
    public Mono<Void> execute(IContext context, IChainExecutor executor)
    {
        return this.handle(context, executor);
    }

    protected abstract Mono<Void> handle(IContext ctx, IChainExecutor executor);
}
