package com.cell.rpc.grpc.client.framework.manager;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.constants.ManagerConstants;
import com.cell.base.core.services.impl.DefaultHandlerMutableChainExecutor;
import com.cell.plugin.pipeline.executor.ChainExecutorFactory;
import com.cell.plugin.pipeline.executor.IListChainExecutor;
import com.cell.plugin.pipeline.manager.AbstractReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 18:04
 */
@Manager(name = ManagerConstants.MANAGER_RPC_CLIENT_HANDLER)
public class GRPCClientHandlerManager extends AbstractReflectManager
{

    private static final GRPCClientHandlerManager instance = new GRPCClientHandlerManager();

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
