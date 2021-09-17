package com.cell.channel;

import com.cell.hooks.IChainExecutor;
import com.cell.protocol.ICommandSuit;
import com.cell.services.Pipeline;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 13:56
 */
public interface IChannel<T, E extends IChainExecutor>
{
    Pipeline<T, E> pipeline();

    Mono<Void> exceptionCaught(ICommandSuit suit, Throwable e);

    void readCommand(ICommandSuit cmdCtx);
}
