package com.cell.manager;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.executor.BaseAutoSelectReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 14:29
 */
@Manager(name = MetricsManager.gatewayMetricsManager)
public class MetricsManager extends BaseAutoSelectReflectManager
{
    public static final String gatewayMetricsManager = "";
    private static final MetricsManager instance = new MetricsManager();

    public static final String postFilterHook = "postFilterHook";

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
