package com.cell.http.framework.manager;

import com.cell.base.core.annotations.Manager;
import com.cell.base.core.executor.BaseAutoSelectReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 04:55
 */
@Manager(name = ReactorSelectorManager.reacotrSelector)
public class ReactorSelectorManager extends BaseAutoSelectReflectManager
{
    public static final String reacotrSelector = "reacotrSelector";
    public static final String selectByUri = "selectByUri";
    public static final String onAddReactor = "onAddReactor";
    private static final ReactorSelectorManager instance = new ReactorSelectorManager();

    public static ReactorSelectorManager getInstance()
    {
        return instance;
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
