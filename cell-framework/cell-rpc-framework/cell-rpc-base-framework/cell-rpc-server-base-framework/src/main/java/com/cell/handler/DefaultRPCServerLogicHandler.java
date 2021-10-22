package com.cell.handler;

import com.cell.annotations.ManagerNode;
import com.cell.constants.ManagerConstants;
import com.cell.executor.IBaseChainExecutor;
import com.cell.services.IHandlerSuit;
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
@ManagerNode(group = ManagerConstants.MANAGER_RPC_HANDLER)
public class DefaultRPCServerLogicHandler extends AbstractRPCServerCmdHandler
{

    @Override
    protected Mono<Void> onHandle(IHandlerSuit context, IBaseChainExecutor executor)
    {
        IRPCHandlerSuit suit = (IRPCHandlerSuit) context;
        System.out.println(suit);
        return executor.execute(context);
    }
}