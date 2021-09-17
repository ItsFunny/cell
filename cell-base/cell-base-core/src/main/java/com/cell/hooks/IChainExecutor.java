package com.cell.hooks;

import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 11:08
 */
public interface IChainExecutor
{
    Mono<Void> execute(IContext context);
}
