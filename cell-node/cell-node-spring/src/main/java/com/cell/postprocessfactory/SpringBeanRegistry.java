package com.cell.postprocessfactory;

import com.cell.config.AbstractInitOnce;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.log.LOG;
import com.cell.models.Module;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.PriorityOrdered;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 00:36
 */
//@Component
public class SpringBeanRegistry extends AbstractInitOnce implements PriorityOrdered, BeanDefinitionRegistryPostProcessor
{
    private static final SpringBeanRegistry instance = new SpringBeanRegistry();

    public static SpringBeanRegistry getInstance()
    {
        return instance;
    }

    // FIXME
    private Set<Class<? extends BeanDefinitionRegistryPostProcessor>> processors = new HashSet<>();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        this.initOnce(null);
        for (Class<? extends BeanDefinitionRegistryPostProcessor> clz : processors)
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(clz);
            String beanName = clz.getName();
            registry.registerBeanDefinition(beanName, beanDefinition);
            LOG.info(Module.CONTAINER, "register bean {} ", beanName);
        }
        // FIXME ,如下述所言
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        // do nothing
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

        // FIXME
        // 需要修改为动态的形式: 提供是否注入为bean ,或者是只是为了临时 作为factory使用
        processors.add(DefaultSpringActivePluginCollector.class);
//        processors.add(SpringDependecyFactoryProcessor.getInstance());
//        processors.add(ExtensionClassFactoryProcessor.getInstance());
//        DefaultSpringActivePluginCollector.getInstance().initOnce(ctx);
    }

    @Override
    public int getOrder()
    {
        return SpringBridge.BEAN_REGISTER_ORDERER;
    }
}
