package com.cell.base.core.center;

import com.cell.base.common.events.IEvent;
import com.cell.base.core.hooks.IEventHook;
import com.cell.base.core.protocol.IEventContext;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.bee.event.center.EventCenter;
import com.cell.bee.event.center.JobCenter;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


public class EventCenterTest
{
    @Data
    @Builder
    static class StringEvent implements IEventContext, IEvent
    {
        String str;

        @Override
        public IEvent getEvent()
        {
            return new A();
        }

        class A implements IEvent
        {

        }
    }

    @Test
    public void testConsumer() throws Exception
    {
        JobCenter.getInstance().registerSubscriber(EventCenter.getInstance());
        IEventHook hook1 = (str, hook) ->
        {
            AbstractEventCenter.DefaultEventWrapper wrapper = (AbstractEventCenter.DefaultEventWrapper) str;
            StringEvent e = (StringEvent) wrapper.getEvent();
            e.str = "hook1_" + e.str;
            System.out.println(e);
            return hook.execute(str);
        };
        IEventHook hook2 = (str, hook) ->
        {
            AbstractEventCenter.DefaultEventWrapper wrapper = (AbstractEventCenter.DefaultEventWrapper) str;
            StringEvent e = (StringEvent) wrapper.getEvent();
            e.str = "hook2_" + e.str;
            System.out.println(e);
            return hook.execute(str);
        };
        EventCenter.getInstance().registerEventHook(hook1);
//        EventCenter.getInstance().registerEventHook(hook2);
        JobCenter.getInstance().addJob(StringEvent.builder().str("123").build());
        JobCenter.getInstance().addJob(StringEvent.builder().str("ssssssssss").build());

        TimeUnit.SECONDS.sleep(10);
    }

}