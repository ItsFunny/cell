package com.cell.postprocessfactory;

import com.cell.config.AbstractInitOnce;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.postprocessors.dependency.SpringBeanDependenciesPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-26 22:26
 */
public class SpringDependecyFactoryProcessor extends AbstractInitOnce implements
        PriorityOrdered, BeanDefinitionRegistryPostProcessor
{
    private static final SpringDependecyFactoryProcessor instance = new SpringDependecyFactoryProcessor();

    public static SpringDependecyFactoryProcessor getInstance()
    {
        return instance;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        SpringBeanDependenciesPostProcessor processor = new SpringBeanDependenciesPostProcessor();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        factory.addBeanPostProcessor(SpringBeanDependenciesPostProcessor.getInstance());
    }

    @Override
    public int getOrder()
    {
        return SpringBridge.BEAN_REGISTER_ORDERER + 1;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
