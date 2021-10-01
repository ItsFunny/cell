package com.cell.handler;

import com.cell.executor.IBaseReactorExecutor;
import com.cell.services.IHandlerSuit;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:18
 */
public interface IHandler extends IBaseReactorExecutor
{
    // 初始化自身
    void init(IHandlerSuit ctx);

    void close(IHandlerSuit ctx);


    Mono<Void> exceptionCaught(IHandlerSuit suit,Throwable e, IChainHandler executor);
}
