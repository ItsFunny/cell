package com.cell.adapter;

import com.cell.discovery.nacos.config.IInitOnce;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 01:48
 */
public interface IBeanDefinitionRegistryPostProcessorAdapter extends BeanDefinitionRegistryPostProcessor, IInitOnce
{
    default  List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor(){
        return null;
    }

    default void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {

    }
}
