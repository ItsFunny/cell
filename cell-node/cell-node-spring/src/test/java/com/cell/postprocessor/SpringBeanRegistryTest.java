package com.cell.postprocessor;

import com.cell.base.core.annotations.Exclude;
import com.cell.base.common.context.InitCTX;
import com.cell.node.spring.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.node.spring.adapter.IBeanPostProcessortAdapter;
import com.cell.node.spring.postprocessor.SpringBeanRegistry;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.List;

public class SpringBeanRegistryTest
{
    public static class A implements IBeanDefinitionRegistryPostProcessorAdapter
    {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException
        {

        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException
        {

        }

        @Override
        public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
        {
            return null;
        }

        @Override
        public void initOnce(InitCTX ctx)
        {
            System.out.println("1");
        }
    }

    @Exclude
    public static class B implements IBeanDefinitionRegistryPostProcessorAdapter
    {

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException
        {

        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException
        {

        }

        @Override
        public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
        {
            return null;
        }

        @Override
        public void initOnce(InitCTX ctx)
        {
            System.out.println("b");
        }
    }

    @Test
    public void onInit()
    {
        SpringBeanRegistry instance = SpringBeanRegistry.getInstance();
        instance.initOnce(null);
        System.out.println(1);
    }
}