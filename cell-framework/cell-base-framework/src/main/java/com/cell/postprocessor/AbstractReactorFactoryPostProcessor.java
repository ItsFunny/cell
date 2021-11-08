package com.cell.postprocessor;

import com.cell.base.core.context.InitCTX;
import com.cell.node.spring.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.node.spring.adapter.IBeanPostProcessortAdapter;
import com.cell.root.Root;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 13:16
 */
public abstract class AbstractReactorFactoryPostProcessor extends AbstractBeanDefiinitionRegistry
{
    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return Arrays.asList(ReactorPostProcessor.class);
    }

    protected abstract List<Class<? extends Annotation>> getTargetAnnotationClasses();

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
    }

    @Override
    public Map<Class<?>, BeanPostProcessor> choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        List<Class<? extends Annotation>> targetAnnotationClasses = this.getTargetAnnotationClasses();
        if (CollectionUtils.isEmpty(targetAnnotationClasses))
        {
            return null;
        }
        for (Class<? extends Annotation> targetAnnotationClass : targetAnnotationClasses)
        {
            List<Class<?>> classes = classListMap.get(targetAnnotationClass);
            if (CollectionUtils.isEmpty(classes)) continue;
            Root.getInstance().addCommands(classes);
        }
        return null;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
