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
 * @Date 创建时间：2021-09-19 20:27
 */
@Manager(name = ManagerConstants.MANAGER_WEB_HANDLER)
public class WebHandlerManager extends AbstractReflectManager
{
    private static final WebHandlerManager instance = new WebHandlerManager();

    public static WebHandlerManager getInstance()
    {
        return instance;
    }


    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }

    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return DefaultHandlerMutableChainExecutor::new;
    }
}
