package com.cell.initializer;

import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.postprocessfactory.SpringDependecyFactoryProcessor;
import com.cell.postprocessfactory.ExtensionClassFactoryProcessor;
import com.cell.postprocessfactory.SpringBeanRegistry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-22 18:00
 */
public class SpringInitializer extends AbstractInitOnce implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {
        this.initOnce(null);
        applicationContext.addBeanFactoryPostProcessor(SpringBeanRegistry.getInstance());
//        applicationContext.addBeanFactoryPostProcessor(SpringDependecyFactoryProcessor.getInstance());
//        applicationContext.addBeanFactoryPostProcessor(ExtensionClassFactoryProcessor.getInstance());
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        SpringBeanRegistry.getInstance().initOnce(ctx);
        SpringDependecyFactoryProcessor.getInstance().initOnce(ctx);
        ExtensionClassFactoryProcessor.getInstance().initOnce(ctx);
    }
}
