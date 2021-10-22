package com.cell.handler;

import com.cell.executor.IBaseChainExecutor;
import com.cell.executor.IChainExecutor;
import com.cell.handler.abs.AbstractHandler;
import com.cell.protocol.IContext;
import com.cell.services.IHandlerSuit;
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
