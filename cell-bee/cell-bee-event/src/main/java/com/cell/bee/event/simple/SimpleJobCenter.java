package com.cell.bee.event.simple;

import com.cell.base.common.events.IEvent;
import com.cell.manager.IReflectManager;
import com.google.common.eventbus.EventBus;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 09:57
 */
public class SimpleJobCenter extends AbstractSimpleEventCenter
{
    private final EventBus bus = new EventBus();

    SimpleJobCenter()
    {
        this.bus.register(this);
    }

    @Override
    protected void afterInvoke()
    {

    }

    public void addJob(IEvent job)
    {
        this.bus.post(job);
    }

    @Override
    public IReflectManager createOrDefault()
    {
        throw new RuntimeException("not supposed yet");
    }
}
