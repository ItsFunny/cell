package com.cell.grpc.server.framework.manager.nodes;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.rpc.common.constants.RPCHookConstants;
import com.cell.rpc.common.gate.hook.AbstractRPCHook;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 13:27
 */
@ManagerNode(group = RPCHookConstants.GROUP_RPC_SERVER_CMD_HOOK, orderValue = Integer.MAX_VALUE)
public class GRPCServerFinalCommandHook extends AbstractRPCHook
{
}
