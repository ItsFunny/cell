package com.cell.monitor.base.updator;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.executor.BaseAutoSelectReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 17:41
 */
@Manager(name = UpdatorManager.updatorManager)
public class UpdatorManager extends BaseAutoSelectReflectManager
{
    public static final String updatorManager = "updatorManager";
    private static final UpdatorManager instance = new UpdatorManager();

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
