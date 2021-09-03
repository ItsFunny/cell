package com.cell.initializer;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.annotations.Plugin;
import com.cell.context.INodeContext;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.models.Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = "com.cell")
//@SpringBootApplication
@CellSpringHttpApplication(scanBasePackages = "com.cell")
public class SpringInitializerTest
{
    public static class B1
    {
        public B1()
        {
            System.out.println("b1");
        }
    }

    public static class B2
    {
        @AutoPlugin
        private B1 b1;

        public B2()
        {
            System.out.println("b2");
        }
    }

    @ActivePlugin
    public static class B3
    {
        @AutoPlugin
        private B2 b2;

        public B3()
        {
            System.out.println("b3");
        }
    }

    public static class MyExtension extends AbstractSpringNodeExtension
    {
        @Plugin
        private B1 b1()
        {
            return new B1();
        }

        @Plugin
        private B2 b2()
        {
            return new B2();
        }

        @Override
        public void init(INodeContext ctx) throws Exception
        {
            LOG.info(Module.COMMON, "init");
        }

        @Override
        public void start(INodeContext ctx) throws Exception
        {
            LOG.info(Module.COMMON, "start");
        }

        @Override
        public void ready(INodeContext ctx) throws Exception
        {
            LOG.info(Module.COMMON, "ready");
        }

        @Override
        public void close(INodeContext ctx) throws Exception
        {
            LOG.info(Module.COMMON, "close");
        }
    }

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(SpringInitializerTest.class, args);
        MyExtension bean = ctx.getBean(MyExtension.class);
        System.out.println(bean);
        B1 b1 = ctx.getBean(B1.class);
        B2 b2 = ctx.getBean(B2.class);
        B3 b3 = ctx.getBean(B3.class);
        System.out.println(b3);
    }
}