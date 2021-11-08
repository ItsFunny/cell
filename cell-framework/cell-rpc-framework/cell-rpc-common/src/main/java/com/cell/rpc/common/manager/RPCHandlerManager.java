package com.cell.rpc.common.manager;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.constants.ManagerConstants;
import com.cell.plugin.pipeline.executor.ChainExecutorFactory;
import com.cell.plugin.pipeline.executor.IListChainExecutor;
import com.cell.base.core.services.impl.DefaultHandlerMutableChainExecutor;
import com.cell.plugin.pipeline.manager.AbstractReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 11:16
 */
@Manager(name = ManagerConstants.MANAGER_RPC_SERVER_HANDLER)
public class RPCHandlerManager extends AbstractReflectManager
{
    private static final RPCHandlerManager instance = new RPCHandlerManager();

    public static RPCHandlerManager getInstance()
    {
        return instance;
    }

    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return DefaultHandlerMutableChainExecutor::new;
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
