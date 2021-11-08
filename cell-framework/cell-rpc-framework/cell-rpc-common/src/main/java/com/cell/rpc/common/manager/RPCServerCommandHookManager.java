package com.cell.rpc.common.manager;

import com.cell.base.core.annotations.Manager;

import com.cell.bee.event.center.AbstractHookCenter;
import com.cell.base.core.hooks.IChainHook;
import com.cell.plugin.pipeline.manager.IReflectManager;
import com.cell.rpc.common.constants.RPCHookConstants;

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
