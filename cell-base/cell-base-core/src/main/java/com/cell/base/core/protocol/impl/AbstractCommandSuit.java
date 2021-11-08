package com.cell.base.core.protocol.impl;

import com.cell.base.core.protocol.CommandContext;
import com.cell.base.core.protocol.ICommandSuit;
import com.cell.base.core.services.impl.AbstractHandlerSuit;
import com.cell.base.core.concurrent.base.EventExecutor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-24 10:45
 */
public abstract class AbstractCommandSuit extends AbstractHandlerSuit implements ICommandSuit
{
    private EventExecutor eventExecutor;

    public AbstractCommandSuit(CommandContext commandContext)
    {
        super(commandContext);
    }


    @Override
    public void setCommandEventExecutor(EventExecutor eventExecutor)
    {
        this.eventExecutor = eventExecutor;
    }

    @Override
    public EventExecutor getCommandEventExecutor()
    {
        return this.eventExecutor;
    }
}
