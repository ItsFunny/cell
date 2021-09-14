package com.cell.manager;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.Manager;
import com.cell.annotations.ManagerNode;
import com.cell.initializer.SpringInitializerTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 13:25
 */
@ComponentScan(basePackages = "com.cell")
@SpringBootApplication
public class ManagerTest
{


    @ManagerNode(group = "mym")
    public static class MYONode
    {

    }

    @ManagerNode(group = "mym", name = "node2")
    @ActivePlugin
    public static class MYONode2
    {

    }

    @Manager(name = "mym")
    public static class MyM extends AbstractReflectManager
    {
        private MyM() {}

        @Override
        protected void onInvokeInterestNodes(Collection<Object> nodes)
        {
            for (Object node : nodes)
            {
                System.out.println(node);
            }
        }

        @Override
        public IReflectManager createOrDefault()
        {
            return new MyM();
        }
    }

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(SpringInitializerTest.class, args);
        MYONode2 bean = ctx.getBean(MYONode2.class);
        System.out.println(bean);
        System.out.println(ctx);
    }

}
