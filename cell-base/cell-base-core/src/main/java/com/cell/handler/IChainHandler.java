package com.cell.handler;

import com.cell.executor.IBaseChainExecutor;
import com.cell.services.IHandlerSuit;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 07:26
 */
public interface IChainHandler extends IBaseChainExecutor
{
    Mono<Void> exceptionCaught(IHandlerSuit suit, Throwable e);
}
