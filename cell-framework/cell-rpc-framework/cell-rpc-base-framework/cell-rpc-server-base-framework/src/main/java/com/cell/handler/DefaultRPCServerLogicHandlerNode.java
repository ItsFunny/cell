package com.cell.handler;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.constants.ManagerConstants;
import com.cell.base.core.executor.IBaseChainExecutor;
import com.cell.manager.RPCServerCommandHookManager;
import com.cell.base.core.services.IHandlerSuit;
import com.cell.suit.IRPCHandlerSuit;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 13:03
 */
@ManagerNode(group = ManagerConstants.MANAGER_RPC_SERVER_HANDLER)
public class DefaultRPCServerLogicHandlerNode extends AbstractRPCServerCmdHandler
{

    @Override
    protected Mono<Void> onHandle(IHandlerSuit context, IBaseChainExecutor executor)
    {
        IRPCHandlerSuit suit = (IRPCHandlerSuit) context;
        return RPCServerCommandHookManager.getInstance().
                execute(suit.getBuzContext()).
                then(executor.execute(context));
    }
}
