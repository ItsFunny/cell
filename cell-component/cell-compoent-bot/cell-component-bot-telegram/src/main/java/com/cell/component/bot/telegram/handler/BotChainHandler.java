package com.cell.component.bot.telegram.handler;

import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.plugin.pipeline.manager.IReflectManager;

public class BotChainHandler extends AbstractEventCenter
{

    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public IReflectManager createOrDefault()
    {
        return null;
    }
}
