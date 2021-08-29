package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.config.AbstractInitOnce;
import com.cell.config.IInitOnce;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.postprocessors.dependency.SpringBeanDependenciesPostProcessor;
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
        PriorityOrdered, IInitOnce
{
    private static final SpringDependecyFactoryProcessor instance = new SpringDependecyFactoryProcessor();

    public static SpringDependecyFactoryProcessor getInstance()
    {
        return instance;
    }

    private class innerInit extends AbstractInitOnce
    {

        @Override
        protected void onInit(InitCTX ctx)
        {

        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        SpringBeanDependenciesPostProcessor processor = new SpringBeanDependenciesPostProcessor();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
//        factory.addBeanPostProcessor(SpringBeanDependenciesPostProcessor.getInstance());
    }

    @Override
    public int getOrder()
    {
        return SpringBridge.BEAN_REGISTER_ORDERER + 1;
    }

    @Override
    public void initOnce(InitCTX ctx)
    {
        new innerInit().initOnce(ctx);
    }

    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        List<Class<? extends IBeanPostProcessortAdapter>> ret = new ArrayList<>();
        ret.add(SpringBeanDependenciesPostProcessor.class);
        return ret;
    }
}
