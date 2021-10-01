package com.cell.executor;

import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 11:08
 */
public interface IChainExecutor<T>
{
    Mono<Void> execute(T t);
}
