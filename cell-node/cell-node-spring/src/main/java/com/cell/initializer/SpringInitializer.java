package com.cell.initializer;

import com.cell.bridge.SpringExtensionManager;
import com.cell.log.LOG;
import com.cell.models.Module;
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
public class SpringInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {
        LOG.info(Module.CONTAINER, "begin");
        applicationContext.getBeanFactory().addBeanPostProcessor(SpringExtensionManager.getInstance());
    }
}
