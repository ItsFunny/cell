package com.cell.plugin.pipeline.executor;

import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 10:56
 */
public interface IReactorExecutor<T> extends SuperClz<T>
{
    Mono<Void> execute(T t, IChainExecutor<T> executor);

    default void executorAdded(T t) {}

    default boolean predict(T t)
    {
        return true;
    }
}
