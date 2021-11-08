package com.cell.rpc.grpc.client.framework.hook;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.hooks.IChainHook;
import com.cell.bee.event.center.AbstractHookCenter;
import com.cell.plugin.pipeline.manager.IReflectManager;
import com.cell.rpc.common.constants.RPCHookConstants;

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
