package com.cell.hooks;

import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 10:56
 */
public interface IReactorExecutor
{
    Mono<Void> execute(IContext context, IChainExecutor executor);

    default void executorAdded(IContext context) {}

    default boolean predict(IContext ctx)
    {
        return true;
    }
}
