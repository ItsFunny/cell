package com.cell.manager;

import com.cell.annotations.Manager;
import com.cell.executor.BaseAutoSelectReflectManager;

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
