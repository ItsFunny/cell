package com.cell.reactor;

import com.cell.context.InitCTX;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 22:21
 */
public  abstract  class AbstractBuzCommandReactor extends AbstractBaseCommandReactor
{
    @Override
    protected void onInit(InitCTX ctx)
    {

    }

    @Override
    public void execute(IContext ctx)
    {
        IBuzzContext context= (IBuzzContext) ctx;
        ICommandReactor reactor = context.getReactor();
    }

    @Override
    public void registerCmd(ICommand cmd)
    {

    }
}
