package com.cell.base.core.reactor;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.core.constants.ContextConstants;
import com.cell.base.core.protocol.ContextResponseWrapper;
import com.cell.base.core.protocol.IBuzzContext;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.protocol.IContext;

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
