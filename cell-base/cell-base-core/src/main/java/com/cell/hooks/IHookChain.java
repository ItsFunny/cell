package com.cell.hooks;

import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:31
 */
public interface IHookChain<T>
{
    Mono<Void> hook(T t);
}
