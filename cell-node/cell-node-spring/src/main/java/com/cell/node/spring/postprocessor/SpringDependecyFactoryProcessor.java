package com.cell.node.spring.postprocessor;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.node.spring.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.node.spring.adapter.IBeanPostProcessortAdapter;
import com.cell.node.spring.constants.SpringBridge;
import com.cell.node.spring.postprocessors.dependency.SpringBeanDependenciesPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.PriorityOrdered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-26 22:26
 */
public class SpringDependecyFactoryProcessor extends AbstractBeanDefiinitionRegistry implements
        PriorityOrdered
{
    private static final SpringDependecyFactoryProcessor instance = new SpringDependecyFactoryProcessor();

    public static SpringDependecyFactoryProcessor getInstance()
    {
        return instance;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }

    private class innerInit extends AbstractInitOnce
    {

        @Override
        protected void onInit(InitCTX ctx)
        {

        }
    }


    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {

    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {

    }


    @Override
    public int getOrder()
    {
        return SpringBridge.BEAN_REGISTER_ORDERER + 1;
    }


    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        List<Class<? extends IBeanPostProcessortAdapter>> ret = new ArrayList<>();
        ret.add(SpringBeanDependenciesPostProcessor.class);
        return ret;
    }
}
