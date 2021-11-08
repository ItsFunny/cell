package com.cell.bee.rate.gateway.sentinel.hook;

import com.cell.base.core.annotations.Manager;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-17 05:26
 */
@Manager(name = MetricsHookManager.METRICS_HOOK)
public class MetricsHookManager extends AbstractEventCenter
{
    public static final String METRICS_HOOK = "METRICS_HOOK";

    private static final MetricsHookManager instance = new MetricsHookManager();

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }

    @Override
    protected void afterInvoke()
    {

    }
}
