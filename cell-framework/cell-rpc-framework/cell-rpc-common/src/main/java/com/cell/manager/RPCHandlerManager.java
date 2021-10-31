package com.cell.manager;

import com.cell.annotations.Manager;
import com.cell.constants.ManagerConstants;
import com.cell.executor.ChainExecutorFactory;
import com.cell.executor.IListChainExecutor;
import com.cell.services.impl.DefaultHandlerMutableChainExecutor;

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
    public static RPCHandlerManager getInstance(){
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
