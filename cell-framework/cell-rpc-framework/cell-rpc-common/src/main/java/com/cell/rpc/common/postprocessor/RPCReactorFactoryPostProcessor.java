package com.cell.rpc.common.postprocessor;

import com.cell.base.common.context.InitCTX;
import com.cell.node.spring.adapter.AbstractBeanDefiinitionRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 13:26
 */
public class RPCReactorFactoryPostProcessor extends AbstractBeanDefiinitionRegistry
{
    @Override
    public Map<Class<?>, BeanPostProcessor> choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        return null;
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
    protected void onInit(InitCTX ctx)
    {

    }
}
