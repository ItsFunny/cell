package com.cell.postprocessor;

import com.cell.adapter.AbstractAutoRegistry;
import com.cell.base.common.annotation.RPCClient;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 10:04
 */
public abstract  class ClientFactoryPostProcessor extends AbstractAutoRegistry
{

    @Override
    protected List<Class<? extends Annotation>> interestAnnotations()
    {
        return Arrays.asList(RPCClient.class);
    }

    @Override
    protected void injectBeforeRegistration(BeanDefinitionRegistry registry, Class<? extends Annotation> ano, Class<?> c)
    {
    }
}
