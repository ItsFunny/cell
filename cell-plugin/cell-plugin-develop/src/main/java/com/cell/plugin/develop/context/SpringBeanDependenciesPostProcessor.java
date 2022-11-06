package com.cell.plugin.develop.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanDependenciesPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
        implements BeanPostProcessor
{

    public SpringBeanDependenciesPostProcessor()
    {

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if (bean instanceof IBeanSelfAware)
        {
            IBeanSelfAware beanSelfAware = (IBeanSelfAware) bean;
            beanSelfAware.setSelf(beanSelfAware);
            return beanSelfAware;
        }
        return super.postProcessAfterInitialization(bean, beanName);
    }
}
