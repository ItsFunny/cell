package com.cell.hooks;

import com.cell.protocol.IContext;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 05:13
 */
public interface IAutoReactorExecutor extends IReactorExecutor
{
    default Mono<Void> execute(String method, IContext ctx) {return Mono.empty();}

    @Override
    default Mono<Void> execute(IContext context, IChainExecutor executor) {return executor.execute(context);}
}
