package com.cell.monitor.impl;

import com.cell.annotations.Manager;
import com.cell.center.AbstractAutoSelectReflectManager;
import com.cell.manager.IReflectManager;
import com.cell.monitor.IMonitor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 10:30
 */
@Manager(name = DefaultMetricsManager.GROUP_METRICS_MONITOR)
public class DefaultMetricsManager extends AbstractAutoSelectReflectManager implements IMonitor
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
