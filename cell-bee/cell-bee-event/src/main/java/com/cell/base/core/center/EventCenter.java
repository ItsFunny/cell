package com.cell.base.core.center;

import com.cell.annotations.Manager;
import com.cell.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description internal使用
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:30
 */
@Manager(name = EventCenter.GROUP_EVENT_CENTER)
public class EventCenter extends AbstractEventCenter
{
    public static final String GROUP_EVENT_CENTER = "GROUP_EVENT_CENTER";
    private static final EventCenter instance = new EventCenter();

    public static EventCenter getInstance()
    {
        return instance;
    }


    @Override
    protected void afterInvoke()
    {
        JobCenter.getInstance().registerSubscriber(this);
    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
