package com.cell.executor;

import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 05:13
 */
public interface IAutoReactorExecutor<T> extends IReactorExecutor<T>
{
    default Mono<Void> execute(String method, T ctx) {return Mono.empty();}

    @Override
    default Mono<Void> execute(T t, IChainExecutor<T> executor) {return executor.execute(t);}
}
