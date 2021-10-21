package com.cell.reactor.abs;

import com.cell.cmd.IRPCServerCommand;
import com.cell.context.IRPCBuzzContext;
import com.cell.context.InitCTX;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;
import com.cell.reactor.AbstractBaseCommandReactor;
import com.cell.reactor.IRPCServerReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 21:38
 */
public abstract class AbstractRPCServerReactor extends AbstractBaseCommandReactor implements IRPCServerReactor
{
    @Override
    protected void onInit(InitCTX ctx)
    {

    }

    @Override
    public void execute(IContext ctx)
    {
        IRPCBuzzContext context = (IRPCBuzzContext) ctx;
        Class<? extends IRPCServerCommand> cmdClz = context.getCommand();
        IRPCServerCommand cmd = null;
        try
        {
            cmd = cmdClz.newInstance();
            context.setReactor(this);
            cmd.execute(context);
        } catch (Exception e)
        {
            context.response(this.createResponseWp().build());
        }
    }

    @Override
    public void registerCmd(ICommand cmd)
    {

    }
}
