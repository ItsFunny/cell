package com.cell.context;

import com.cell.cmd.IRPCServerCommand;
import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-24 22:17
 */
public class RPCServerCommandBuzzContext extends AbstractBaseContext implements IRPCBuzzContext
{

    public RPCServerCommandBuzzContext(CommandContext commandContext)
    {
        super(commandContext);
    }

    @Override
    public Class<? extends IRPCServerCommand> getCommand()
    {
        return null;
    }

    @Override
    public void discard()
    {

    }
}
