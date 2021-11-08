package com.cell.rpc.grpc.client.framework.postprocessors;

import com.cell.base.core.context.InitCTX;
import com.cell.node.spring.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.root.Root;
import com.cell.rpc.grpc.client.framework.annotation.GRPCClientRequestAnno;
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
 * @Date 创建时间：2021-11-04 05:12
 */
public class GRPCClientFactoryPostProcessor extends AbstractBeanDefiinitionRegistry
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

    @Override
    public Map<Class<?>, BeanPostProcessor> choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        List<Class<?>> requests = classListMap.get(GRPCClientRequestAnno.class);
        Root.getInstance().addAnnotationClasses(GRPCClientRequestAnno.class, requests);
        return null;
    }
}
