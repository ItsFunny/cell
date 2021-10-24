package com.cell.protocol.impl;

import com.cell.concurrent.base.EventExecutor;
import com.cell.protocol.CommandContext;
import com.cell.protocol.ICommandSuit;
import com.cell.services.impl.AbstractHandlerSuit;

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
