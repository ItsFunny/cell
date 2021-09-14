package com.cell.center;

import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.utils.ReflectUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class EventCenterTest
{
    @Data
    @Builder
    static class StringEvent implements IEvent
    {
        String str;
    }

    @Test
    public void testConsumer() throws Exception
    {
        JobCenter.getInstance().registerSubscriber(EventCenter.getInstance());
        IEventHook hook1 = (str, hook) ->
        {
            StringEvent e = (StringEvent) str;
            e.str = "hook1_" + e.str;
            System.out.println(e);
            return hook.hook(e);
        };
        IEventHook hook2 = (str, hook) ->
        {
            StringEvent e = (StringEvent) str;
            e.str = "hook2_" + e.str;
            System.out.println(e);
            return hook.hook(str);
        };
        EventCenter.getInstance().registerEventHook(hook1);
        EventCenter.getInstance().registerEventHook(hook2);
        JobCenter.getInstance().addJob(StringEvent.builder().str("123").build());
        JobCenter.getInstance().addJob(StringEvent.builder().str("ssssssssss").build());

        TimeUnit.SECONDS.sleep(10);
    }

}