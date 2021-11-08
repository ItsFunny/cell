package com.cell.http.gate.hook.node;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.constants.RPCHookConstants;
import com.cell.http.gate.hook.AbstractRPCHook;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:48
 */
@ManagerNode(group = RPCHookConstants.GROUP_RPC_CLIENT_CMD_HOOK, orderValue = Integer.MAX_VALUE)
public class RPCClientFinalHook extends AbstractRPCHook
{

}
