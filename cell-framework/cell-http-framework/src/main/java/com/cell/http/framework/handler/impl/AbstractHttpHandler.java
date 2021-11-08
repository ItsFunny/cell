package com.cell.http.framework.handler.impl;

import com.cell.base.core.executor.IBaseChainExecutor;
import com.cell.base.core.handler.abs.AbstractHandler;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.services.IHandlerSuit;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.http.framework.handler.IHttpCmdHandler;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 06:08
 */
public abstract class AbstractHttpHandler extends AbstractHandler implements IHttpCmdHandler
{
    @Override
    protected Mono<Void> handle(IContext ctx, IChainExecutor executor)
    {
        return this.onHandle((IHandlerSuit) ctx, (IBaseChainExecutor) executor);
    }

    protected abstract Mono<Void> onHandle(IHandlerSuit context, IBaseChainExecutor executor);
}
