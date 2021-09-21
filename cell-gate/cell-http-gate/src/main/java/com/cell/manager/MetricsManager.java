package com.cell.manager;

import com.cell.annotations.Manager;
import com.cell.center.AbstractAutoSelectReflectManager;
import com.cell.center.AbstractHookCenter;
import reactor.util.Metrics;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 14:29
 */
@Manager(name = MetricsManager.gatewayMetricsManager)
public class MetricsManager extends AbstractAutoSelectReflectManager
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
