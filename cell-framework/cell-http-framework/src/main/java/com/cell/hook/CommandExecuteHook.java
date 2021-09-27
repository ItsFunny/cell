package com.cell.hook;

import com.cell.annotations.ManagerNode;
import com.cell.constant.HookConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.hooks.IChainHook;
import com.cell.reactor.IHttpReactor;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:12
 */
@ManagerNode(group = HookConstants.GROUP_CMD_HOOK,
        name = HookConstants.NAME_HOOK,
        orderValue = Integer.MAX_VALUE)
public class CommandExecuteHook extends AbstractHttpCommandHook
{


    @Override
    protected Mono<Void> onHook(IHttpCommandContext ctx, IChainHook hook)
    {
        IHttpReactor reactor = ctx.getHttpReactor();
        reactor.execute(ctx);
        return hook.execute(ctx).doOnError(this::onExceptionCaught).then(Mono.fromRunnable(() ->
        {
        }));
    }
}
