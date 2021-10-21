package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.context.InitCTX;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-09 06:02
 */
public class RPCFactoryPostProcessor extends AbstractBeanDefiinitionRegistry
{

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {

    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {

    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
