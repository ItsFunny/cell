package com.cell.context;

import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandContext;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:34
 */
public abstract class AbstractHttpCommandContext extends AbstractBaseContext
{
    protected CommandContext commandContext;

    public AbstractHttpCommandContext(CommandContext commandContext)
    {
        this.commandContext = commandContext;
    }

    @Override
    public void discard() throws IOException
    {
        this.commandContext.discard();
    }
}
