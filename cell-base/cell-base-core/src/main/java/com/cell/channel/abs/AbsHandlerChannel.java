package com.cell.channel.abs;

import com.cell.channel.IChannel;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.pipeline.DefaultPipeline;
import com.cell.pipeline.Pipeline;
import com.cell.protocol.ICommandSuit;
import com.cell.services.impl.DefaultHookMutableChainExecutor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 20:49
 */
public abstract class AbsHandlerChannel implements IChannel<IHandler, IChainHandler>
{
    private Pipeline<IHandler, IChainHandler> pipeline;

    private AbsHandlerChannel()
    {
        this.pipeline = new DefaultPipeline<>(DefaultHookMutableChainExecutor::new);
    }


    @Override
    public Pipeline<IHandler, IChainHandler> pipeline()
    {
        return this.pipeline;
    }

    @Override
    public Mono<Void> exceptionCaught(ICommandSuit suit, Throwable e)
    {
        return this.pipeline.chainExecutor().exceptionCaught(suit, e);
    }

    @Override
    public void readCommand(ICommandSuit cmdCtx)
    {
        this.pipeline.chainExecutor().execute(cmdCtx).onErrorResume((e) ->
                this.exceptionCaught(cmdCtx, e)).subscribe();
    }
}
