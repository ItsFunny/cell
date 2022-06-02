package com.cell.base.core.timewheel;

import io.netty.util.Timeout;
import reactor.core.publisher.Mono;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 06:28
 */
@FunctionalInterface
public interface TaskFuncs
{
    Mono<Void> handler(Timeout task);
}
