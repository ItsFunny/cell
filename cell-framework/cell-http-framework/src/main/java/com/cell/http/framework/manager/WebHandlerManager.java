package com.cell.http.framework.manager;

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
