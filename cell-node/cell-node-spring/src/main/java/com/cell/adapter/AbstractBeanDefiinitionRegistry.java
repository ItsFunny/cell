package com.cell.adapter;

import com.cell.grpc.common.config.AbstractInitOnce;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 03:00
 */
public abstract class AbstractBeanDefiinitionRegistry extends AbstractInitOnce implements IBeanDefinitionRegistryPostProcessorAdapter
{

    public AbstractBeanDefiinitionRegistry()
    {

    }

//    private boolean ready;

//    protected String createPostBeanName(Class<? extends IBeanPostProcessortAdapter> aClass)
//    {
//        return beanPostPrefix + "_" + aClass.getName();
//    }

//    private void registerSelfBoundle(BeanDefinitionRegistry registry)
//    {
//        if (ready) return;
//        this.onRegisterSelfBoundle(registry);
//        this.ready = true;
//    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
//        this.registerSelfBoundle(registry);
        this.onPostProcessBeanDefinitionRegistry(registry);
    }

    protected abstract void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);

    protected abstract void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        this.onPostProcessBeanFactory(factory);
    }

    protected void defaultRegisterBean(BeanDefinitionRegistry registry, Class<?> clz)
    {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(clz);
        String beanName = clz.getName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}
