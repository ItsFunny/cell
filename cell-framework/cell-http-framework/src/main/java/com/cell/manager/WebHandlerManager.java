package com.cell.manager;

import com.cell.annotations.Manager;
import com.cell.center.AbstractReflectManager;
import com.cell.constants.ManagerConstants;
import com.cell.hooks.IListChainExecutor;
import com.cell.services.ChainExecutorFactory;
import com.cell.services.impl.DefaultHandlerMutableChainExecutor;
import lombok.Data;

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
