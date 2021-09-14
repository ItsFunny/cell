package com.cell.factory;

import com.cell.manager.IManagerFactory;
import com.cell.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 16:53
 */
public class InternalEventHookFactory  implements IManagerFactory
{

    @Override
    public IReflectManager createInstance()
    {
        return null;
    }
}
