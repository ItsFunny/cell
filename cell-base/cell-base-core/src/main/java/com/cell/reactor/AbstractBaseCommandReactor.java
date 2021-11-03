package com.cell.reactor;

import com.cell.discovery.nacos.config.AbstractInitOnce;
import com.cell.constants.ContextConstants;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 15:46
 */
public abstract class AbstractBaseCommandReactor extends AbstractInitOnce implements ICommandReactor
{
    protected ContextResponseWrapper.ContextResponseWrapperBuilder createResponseWp()
    {
        return ContextResponseWrapper.builder();
    }

    @Override
    public void execute(IContext context)
    {
        IBuzzContext ctx = (IBuzzContext) context;

        Class<? extends ICommand> cmdClz = ctx.getCommandContext().getWrapper().getCmd();
        ICommand cmd = null;
        try
        {
            // FIXME optimize
            cmd = cmdClz.newInstance();
            cmd.execute(ctx);
        } catch (Exception e)
        {
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.FAIL)
                    .cmd(cmd)
                    .exception(e)
                    .build());
        }
    }
}
