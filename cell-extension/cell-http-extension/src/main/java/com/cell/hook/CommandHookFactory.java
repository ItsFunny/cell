package com.cell.hook;

import com.cell.manager.IManagerFactory;
import com.cell.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 15:07
 */
public class CommandHookFactory implements IManagerFactory
{
    @Override
    public IReflectManager createInstance()
    {
        return CmdHookManager.getInstance();
    }
}
