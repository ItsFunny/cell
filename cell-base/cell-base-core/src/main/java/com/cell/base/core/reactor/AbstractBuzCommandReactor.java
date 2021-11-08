package com.cell.base.core.reactor;

import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.context.InitCTX;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 22:21
 */
public abstract class AbstractBuzCommandReactor extends AbstractBaseCommandReactor
{
    @Override
    protected void onInit(InitCTX ctx)
    {

    }


    @Override
    public void registerCmd(ICommand cmd)
    {

    }
}
