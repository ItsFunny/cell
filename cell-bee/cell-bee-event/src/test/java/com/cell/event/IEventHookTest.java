package com.cell.event;

import com.cell.events.IEvent;
import com.cell.hooks.IEventHook;
import com.cell.hooks.IHookChain;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


public class IEventHookTest
{

    public static class AA implements IEvent
    {

    }

//    public static class DemoStringExchange extends AbstractReactHookExchangeFactory<String, Mono<String>>
//    {
//
//    }


    @Test
    public void testFlux()
    {
    }


    interface MyEventListener<T>
    {
        void onDataChunk(List<T> chunk);

        void processComplete();
    }

    class MyEventProcessor
    {

    }


    class DefaultHookChain implements IHookChain<String>
    {
        private List<IEventHook<String>> hooks;
        private final int index;

        DefaultHookChain(List<IEventHook<String>> hooks)
        {
            this.hooks = hooks;
            this.index = 0;
        }

        private DefaultHookChain(DefaultHookChain parent, int index)
        {
            this.hooks = parent.hooks;
            this.index = index;
        }


        @Override
        public Mono<Void> hook(String str)
        {
            return Mono.defer(() ->
            {
                if (this.index < this.hooks.size())
                {
                    IEventHook<String> h = this.hooks.get(this.index);
                    DefaultHookChain hh = new DefaultHookChain(this,
                            this.index + 1);
                    return h.hook(str, hh);
                } else
                {
                    return Mono.empty();
                }
            });
        }
    }

    @Test
    public void testChain()
    {
        IEventHook<String> hook1 = (str, hook) ->
        {
            str = "hook1_" + str;
            System.out.println(str);
            return hook.hook(str);
        };
        IEventHook hook2 = (str, hook) ->
        {
            str = "hook2_" + str;
            System.out.println(str);
            return hook.hook(str);
        };
        DefaultHookChain chain = new DefaultHookChain(Arrays.asList(hook1, hook2));
        Mono<Void> hook = chain.hook("123");
        hook.subscribe();
    }


}