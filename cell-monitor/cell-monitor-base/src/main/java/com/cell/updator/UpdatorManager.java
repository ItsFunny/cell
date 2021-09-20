package com.cell.updator;

import com.cell.annotations.Manager;
import com.cell.center.AbstractAutoSelectReflectManager;
import com.cell.manager.IReflectManager;
import swxa.G;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 17:41
 */
@Manager(name = UpdatorManager.updatorManager)
public class UpdatorManager extends AbstractAutoSelectReflectManager
{
    public static final String updatorManager = "updatorManager";
    private static final UpdatorManager instance = new UpdatorManager();

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
