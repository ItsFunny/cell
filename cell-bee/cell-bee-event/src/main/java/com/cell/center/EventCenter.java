package com.cell.center;

import com.cell.annotations.Manager;
import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import com.cell.manager.AbstractReflectManager;
import com.cell.manager.IReflectManager;
import com.cell.utils.CollectionUtils;
import com.google.common.eventbus.Subscribe;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public IReflectManager createOrDefault()
    {
        return instance;
    }
}
