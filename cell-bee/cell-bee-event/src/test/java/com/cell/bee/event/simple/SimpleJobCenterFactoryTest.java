package com.cell.bee.event.simple;

import com.cell.events.IEvent;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SimpleJobCenterFactoryTest
{
    @Builder
    @Data
    public static class AEventJob implements IEvent
    {
        private String name = "sd";
    }

    @Test
    public void testSimple() throws Exception
    {
        SimpleJobCenter simpleJobCenter = SimpleJobCenterFactory.NewSimpleJobCenter();
        ISimpleEventHook hook1 = (event, executor) ->
        {
            System.out.println("hoo1:" + event);
            return executor.execute(event);
        };
        ISimpleEventHook hook2 = (event, executor) ->
        {
            System.out.println("hook2:" + event);
            return executor.execute(event);
        };

        simpleJobCenter.registerEventHook(hook1);
        simpleJobCenter.registerEventHook(hook2);
        simpleJobCenter.addJob(AEventJob.builder().name("charlie").build());

        TimeUnit.SECONDS.sleep(10);
    }

}