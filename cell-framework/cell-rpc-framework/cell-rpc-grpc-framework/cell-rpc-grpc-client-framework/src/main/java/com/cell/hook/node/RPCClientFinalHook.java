package com.cell.hook.node;

import com.cell.annotations.ManagerNode;
import com.cell.constants.RPCHookConstants;
import com.cell.hook.AbstractRPCHook;

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
