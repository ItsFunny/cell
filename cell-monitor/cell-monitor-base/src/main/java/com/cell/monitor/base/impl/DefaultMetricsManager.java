package com.cell.monitor.base.impl;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.executor.BaseAutoSelectReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;
import com.cell.monitor.base.IMonitor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 10:30
 */
@Manager(name = DefaultMetricsManager.GROUP_METRICS_MONITOR)
public class DefaultMetricsManager extends BaseAutoSelectReflectManager implements IMonitor
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
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
