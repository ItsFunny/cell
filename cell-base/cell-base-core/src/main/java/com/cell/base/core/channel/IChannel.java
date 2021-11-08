package com.cell.base.core.channel;

import com.cell.base.core.protocol.ICommandSuit;
import com.cell.base.core.protocol.IContext;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.pipeline.Pipeline;
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
