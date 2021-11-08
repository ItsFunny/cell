package com.cell.monitor.base.executor;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.executor.BaseAutoSelectReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 17:44
 */
@Manager(name = MetricsManager.metricsManager)
public class MetricsManager extends BaseAutoSelectReflectManager
{
    public static final String metricsManager = "MetricsManager";
    private static final MetricsManager instance = new MetricsManager();

    public static MetricsManager getInstance()
    {
        return instance;
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
