package com.cell.manager;

import com.cell.base.core.annotations.ActivePlugin;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.initializer.SpringInitializerTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

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


    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(SpringInitializerTest.class, args);
        MYONode2 bean = ctx.getBean(MYONode2.class);
        System.out.println(bean);
        System.out.println(ctx);
    }

}
