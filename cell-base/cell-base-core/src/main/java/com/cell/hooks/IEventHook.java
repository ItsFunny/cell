package com.cell.hooks;

import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:33
 */
public interface IEventHook<T>
{
    Mono<Void> hook(T t, IHookChain hook);
}
