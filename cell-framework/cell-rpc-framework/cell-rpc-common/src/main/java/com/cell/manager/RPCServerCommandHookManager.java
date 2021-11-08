package com.cell.manager;

import com.cell.annotations.Manager;
import com.cell.bee.event.center.AbstractHookCenter;
import com.cell.constants.RPCHookConstants;
import com.cell.hooks.IChainHook;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 13:24
 */
@Manager(name = RPCHookConstants.GROUP_RPC_SERVER_CMD_HOOK)
public class RPCServerCommandHookManager extends AbstractHookCenter implements IChainHook
{
    private static final RPCServerCommandHookManager instance = new RPCServerCommandHookManager();

    public static RPCServerCommandHookManager getInstance()
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
