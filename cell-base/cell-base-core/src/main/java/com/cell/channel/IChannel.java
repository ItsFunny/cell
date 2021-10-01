package com.cell.channel;

import com.cell.executor.IChainExecutor;
import com.cell.pipeline.Pipeline;
import com.cell.protocol.ICommandSuit;
import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 13:56
 */
public interface IChannel<T, E extends IChainExecutor<IContext>>
{
    Pipeline<T, E> pipeline();

    Mono<Void> exceptionCaught(ICommandSuit suit, Throwable e);

    void readCommand(ICommandSuit cmdCtx);
}
