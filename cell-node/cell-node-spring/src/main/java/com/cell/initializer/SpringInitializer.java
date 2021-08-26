package com.cell.initializer;

import com.cell.bridge.SpringExtensionManager;
import com.cell.postprocessfactory.DefaultSpringActivePluginCollector;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
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
public class SpringInitializer extends AbstractInitOnce implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {
        this.initOnce(null);
        LOG.info(Module.CONTAINER, "begin");
        applicationContext.addBeanFactoryPostProcessor(DefaultSpringActivePluginCollector.getInstance());
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        DefaultSpringActivePluginCollector.getInstance().initOnce(ctx);
    }
}
