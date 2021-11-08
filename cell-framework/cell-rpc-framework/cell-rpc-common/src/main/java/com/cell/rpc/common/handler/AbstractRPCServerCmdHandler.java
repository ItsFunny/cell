package com.cell.rpc.common.handler;

import com.cell.base.core.executor.IBaseChainExecutor;
import com.cell.base.core.handler.abs.AbstractHandler;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.services.IHandlerSuit;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 11:24
 */
public abstract class AbstractRPCServerCmdHandler extends AbstractHandler implements IRPCServerCmdHandler
{
    @Override
    protected Mono<Void> handle(IContext ctx, IChainExecutor executor)
    {
        return this.onHandle((IHandlerSuit) ctx, (IBaseChainExecutor) executor);
    }

    protected abstract Mono<Void> onHandle(IHandlerSuit context, IBaseChainExecutor executor);

}
