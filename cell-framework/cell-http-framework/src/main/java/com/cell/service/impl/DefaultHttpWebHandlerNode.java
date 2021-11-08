package com.cell.service.impl;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.constants.ManagerConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.context.IHttpHandlerSuit;
import com.cell.exceptions.IHttpExceptionResolver;
import com.cell.base.core.executor.IBaseChainExecutor;
import com.cell.base.core.handler.IChainHandler;
import com.cell.handler.impl.AbstractHttpHandler;
import com.cell.http.gate.hook.CommandHookManager;
import com.cell.base.core.services.IHandlerSuit;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 14:47
 */
@ManagerNode(group = ManagerConstants.MANAGER_WEB_HANDLER, name = "web")
@CellOrder(value = OrderConstants.MAX_ORDER)
public class DefaultHttpWebHandlerNode extends AbstractHttpHandler
{
    @AutoPlugin
    private IHttpExceptionResolver exceptionResolver;

    @Override
    protected Mono<Void> onHandle(IHandlerSuit context, IBaseChainExecutor executor)
    {
        IHttpHandlerSuit suit = (IHttpHandlerSuit) context;
        return CommandHookManager.getInstance().
                execute(suit.getBuzContext()).
                then(executor.execute(context));
    }

    @Override
    public Mono<Void> exceptionCaught(IHandlerSuit suit, Throwable e, IChainHandler executor)
    {
        return super.exceptionCaught(suit, e, executor).then(Mono.fromRunnable(() ->
        {
            IHttpHandlerSuit httpHandlerSuit = (IHttpHandlerSuit) suit;
            IHttpCommandContext bzCtx = (IHttpCommandContext) httpHandlerSuit.getBuzContext();
            this.exceptionResolver.exceptionCaught(e, bzCtx);
        }));
    }
}
