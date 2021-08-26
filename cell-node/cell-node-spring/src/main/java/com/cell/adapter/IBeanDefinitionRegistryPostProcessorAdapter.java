package com.cell.adapter;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 01:48
 */
public interface IBeanDefinitionRegistryPostProcessorAdapter extends BeanDefinitionRegistryPostProcessor
{
    List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor();
}
