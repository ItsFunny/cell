package com.cell.executor;

import com.cell.annotations.Manager;
import com.cell.manager.IReflectManager;

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
