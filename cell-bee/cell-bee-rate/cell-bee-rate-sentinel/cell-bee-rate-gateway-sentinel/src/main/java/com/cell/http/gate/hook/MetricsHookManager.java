package com.cell.http.gate.hook;

import com.cell.annotations.Manager;
import com.cell.base.core.center.AbstractEventCenter;
import com.cell.manager.IReflectManager;

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
