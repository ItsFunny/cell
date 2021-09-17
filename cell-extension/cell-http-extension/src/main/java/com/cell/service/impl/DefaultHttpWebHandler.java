package com.cell.service.impl;

import com.cell.annotations.ManagerNode;
import com.cell.constants.ManagerConstants;
import com.cell.context.IHttpHandlerSuit;
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
public class DefaultHttpWebHandler extends AbstractHttpHandler
{

    @Override
    protected Mono<Void> onHandle(IHandlerSuit context)
    {
//        IHttpContext httpContext = ctx.getBuzContext();
//        LOG.info(Module.HTTP_FRAMEWORK, "开始转为buzzContext.res:{}");
//        return handler.execute(context);
        IHttpHandlerSuit suit = (IHttpHandlerSuit) context;
        return CommandHookManager.getInstance().execute(suit.getBuzContext());
    }

}
