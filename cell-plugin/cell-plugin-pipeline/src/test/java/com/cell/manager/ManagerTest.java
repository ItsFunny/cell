package com.cell.manager;

import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.annotation.ActiveMethod;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import com.cell.plugin.pipeline.manager.AbstractAutoSelectReflectManager;
import com.cell.plugin.pipeline.manager.IReflectManager;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-29 20:11
 */
public class ManagerTest
{
    public static class Manager1 extends AbstractAutoSelectReflectManager<Object>
    {
        private static final Manager1 instance = new Manager1();

        @Override
        public IReflectManager createOrDefault()
        {
            return instance;
        }
    }

    public static class Node1
    {
        @ActiveMethod(id = "1", description = "")
        public IReactorExecutor<Object> m1()
        {
            return (str, c) ->
            {
                System.out.println(str);
                throw new RuntimeException("asd");
            };
        }
    }

    public static class Node2
    {
        @ActiveMethod(id = "1", description = "")
        public IReactorExecutor<Object> m1()
        {
            return (str, c) ->
            {
                System.out.println("node2:" + str);
                return c.execute(str);
            };
        }
    }

    @Test
    public void testPrint()
    {
        try
        {
            Manager1 manager1 = new Manager1();
            manager1.invokeInterestNodes(Arrays.asList(new Node1(), new Node2()));
            manager1.execute("1", "123").block();
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
