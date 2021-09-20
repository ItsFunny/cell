package com.cell.center;

import com.cell.annotations.ActiveMethod;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.manager.IReflectManager;
import com.cell.protocol.IContext;
import com.cell.services.Pipeline;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AbstractAutoSelectReflectManagerTest
{
    public static class AAA extends AbstractAutoSelectReflectManager
    {

        @Override
        public IReflectManager createOrDefault()
        {
            return null;
        }

    }

    public static class ABC
    {
        @ActiveMethod(id = 1, description = "test")
        public IReactorExecutor test()
        {
            return (ctx, ec) ->
            {
                System.out.println("123");
                return ec.execute(ctx);
            };
        }
    }


    @Test
    public void testAutho()
    {

        AAA a = new AAA();
        ABC ab = new ABC();
        a.invokeInterestNodes(Arrays.asList(ab));

        Mono<Void> execute = a.execute(1, IContext.EMPTY_CONTEXT);
        execute.subscribe();
    }

}