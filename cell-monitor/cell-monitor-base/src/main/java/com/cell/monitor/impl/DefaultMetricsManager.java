package com.cell.monitor.impl;

import com.cell.annotations.Manager;
import com.cell.center.AbstractHandlerCenter;
import com.cell.center.AbstractReflectManager;
import com.cell.executor.IChainMetricsExecutor;
import com.cell.executor.IMetricsExecutor;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IListChainExecutor;
import com.cell.manager.IReflectManager;
import com.cell.monitor.IMonitor;
import com.cell.services.ChainExecutorFactory;
import com.cell.services.impl.DefaultCommonMutableChainExecutor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 10:30
 */
@Manager(name = DefaultMetricsManager.GROUP_METRICS_MONITOR)
public class DefaultMetricsManager extends AbstractReflectManager<IMetricsExecutor, IChainMetricsExecutor> implements IMonitor
{
    private static final DefaultMetricsManager instance = new DefaultMetricsManager();
    public static final String GROUP_METRICS_MONITOR = "GROUP_PROMETHEUS_MONITOR";

    public static DefaultMetricsManager getInstance()
    {
        return instance;
    }

    @Override
    public void updateMetrics()
    {
//        this.pipeline.chainExecutor().execute()
    }

    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return () -> new DefaultCommonMutableChainExecutor();
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
