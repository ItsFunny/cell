package com.cell.handler.impl;

import com.cell.handler.IHttpCmdHandler;
import com.cell.handler.abs.AbstractHandler;
import com.cell.hooks.IChainExecutor;
import com.cell.protocol.IContext;
import com.cell.services.IHandlerSuit;
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
        return this.onHandle((IHandlerSuit) ctx,executor);
    }

    protected abstract Mono<Void> onHandle(IHandlerSuit context,IChainExecutor executor);
}
