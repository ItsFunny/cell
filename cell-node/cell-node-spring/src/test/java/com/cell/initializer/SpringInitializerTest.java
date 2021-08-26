package com.cell.initializer;

import cn.tass.math.raw.Mod;
import com.cell.annotation.ActivePlugin;
import com.cell.bridge.SpringExtensionManager;
import com.cell.context.INodeContext;
import com.cell.extension.AbstractNodeExtension;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.postprocessfactory.DefaultSpringActivePluginCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.cell")
@SpringBootApplication
public class SpringInitializerTest
{
    public static class MyExtension extends AbstractSpringNodeExtension
    {
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
    }
}