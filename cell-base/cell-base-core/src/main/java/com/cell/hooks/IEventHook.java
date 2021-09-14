package com.cell.hooks;

import com.cell.events.IEvent;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:33
 */
// FIXME ,REMOVE GENSIS
public interface IEventHook
{
    Mono<Void> hook(IEvent t, IHookChain hook);

    default boolean predict(IEvent t)
    {
        return true;
    }
}
