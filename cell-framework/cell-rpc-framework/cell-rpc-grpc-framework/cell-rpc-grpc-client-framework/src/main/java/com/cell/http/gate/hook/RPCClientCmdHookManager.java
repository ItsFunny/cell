package com.cell.http.gate.hook;

import com.cell.annotations.Manager;
import com.cell.center.AbstractHookCenter;
import com.cell.constants.RPCHookConstants;
import com.cell.hooks.IChainHook;
import com.cell.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:47
 */
@Manager(name = RPCHookConstants.GROUP_RPC_CLIENT_CMD_HOOK)
public class RPCClientCmdHookManager extends AbstractHookCenter implements IChainHook
{
    private static final RPCClientCmdHookManager instance = new RPCClientCmdHookManager();

    public static RPCClientCmdHookManager getInstance()
    {
        return instance;
    }

    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
