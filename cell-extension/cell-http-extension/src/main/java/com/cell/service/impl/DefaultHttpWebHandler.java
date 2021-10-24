package com.cell.service.impl;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.CellOrder;
import com.cell.annotations.ManagerNode;
import com.cell.constants.ManagerConstants;
import com.cell.constants.OrderConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.context.IHttpHandlerSuit;
import com.cell.exceptions.IHttpExceptionResolver;
import com.cell.executor.IBaseChainExecutor;
import com.cell.handler.IChainHandler;
import com.cell.handler.impl.AbstractHttpHandler;
import com.cell.hook.CommandHookManager;
import com.cell.services.IHandlerSuit;
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
public class DefaultHttpWebHandler extends AbstractHttpHandler
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
