package com.cell.adapter;

import com.cell.context.InitCTX;
import com.cell.utils.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 10:24
 */
public abstract class AbstractAutoRegistry extends AbstractBeanDefiinitionRegistry
{
    protected abstract List<Class<? extends Annotation>> interestAnnotations();

    private Map<Class<? extends Annotation>, Set<Class<?>>> classes = new HashMap<>();

    @Override
    public void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        List<Class<? extends Annotation>> annotations = this.interestAnnotations();
        if (CollectionUtils.isEmpty(annotations)) return;
        for (Class<? extends Annotation> annotation : annotations)
        {
            List<Class<?>> classes = classListMap.get(annotation);
            if (CollectionUtils.isEmpty(classes)) continue;
            Set<Class<?>> classSet = this.classes.get(annotation);
            if (CollectionUtils.isEmpty(classSet))
            {
                classSet = new HashSet<>();
                this.classes.put(annotation, classSet);
            }
            classSet.addAll(classes);
        }
    }

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
        if (classes.isEmpty())
        {
            return;
        }
        Set<Class<? extends Annotation>> annotations = this.classes.keySet();
        for (Class<? extends Annotation> annotation : annotations)
        {
            Set<Class<?>> classSet = this.classes.get(annotation);
            classSet.forEach(p ->
            {
                this.injectBeforeRegistration(registry, annotation, p);
                this.defaultRegisterBean(registry, p);
            });
        }
    }

    protected void injectBeforeRegistration(BeanDefinitionRegistry registry, Class<? extends Annotation> ano, Class<?> c) {}

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}