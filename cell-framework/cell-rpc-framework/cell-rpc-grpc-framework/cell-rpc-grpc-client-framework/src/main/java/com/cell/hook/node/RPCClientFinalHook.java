package com.cell.hook.node;

import com.cell.annotations.ManagerNode;
import com.cell.constants.RPCHookConstants;
import com.cell.context.IRPCContext;
import com.cell.hook.AbstractRPCHook;
import com.cell.hooks.IChainHook;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:48
 */
@ManagerNode(group = RPCHookConstants.GROUP_RPC_CMD_HOOK, orderValue = Integer.MAX_VALUE)
public class RPCClientFinalHook extends AbstractRPCHook
{



    @Override
    protected Mono<Void> doHook(IRPCContext context, IChainHook hook)
    {
        context.getReactor().execute(context);
        return hook.execute(context).doOnError(this::onExceptionCaught).then(Mono.fromRunnable(() ->
        {
        }));
    }
}
