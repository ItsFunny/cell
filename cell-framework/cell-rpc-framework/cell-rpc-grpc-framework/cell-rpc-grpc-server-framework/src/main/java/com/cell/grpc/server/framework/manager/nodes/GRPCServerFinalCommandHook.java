package com.cell.grpc.server.framework.manager.nodes;

import com.cell.annotations.ManagerNode;
import com.cell.constants.RPCHookConstants;
import com.cell.http.gate.hook.AbstractRPCHook;

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
