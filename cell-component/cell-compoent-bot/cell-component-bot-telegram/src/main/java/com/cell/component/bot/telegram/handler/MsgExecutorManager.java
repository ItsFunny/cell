package com.cell.component.bot.telegram.handler;


import com.cell.base.core.annotations.Manager;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.plugin.pipeline.manager.IReflectManager;

import java.util.Collection;

@Manager(name = MsgExecutorManager.receiveUpdate)
public class MsgExecutorManager extends AbstractEventCenter
{
    public static final String receiveUpdate = "receiveUpdate";
    private static final MsgExecutorManager instance = new MsgExecutorManager();

    public static MsgExecutorManager getInstance()
    {
        return instance;
    }

    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        super.invokeInterestNodes(nodes);
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
