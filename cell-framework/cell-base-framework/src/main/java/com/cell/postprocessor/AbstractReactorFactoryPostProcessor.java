package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.context.InitCTX;
import com.cell.root.Root;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

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

    protected abstract Class<? extends Annotation> getTargetAnnotationClasses();

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {


    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {

    }

    @Override
    public void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {
        Class<? extends Annotation> targetAnnotationClasses = this.getTargetAnnotationClasses();
        List<Class<?>> cmds = classListMap.get(targetAnnotationClasses);
        Root.getInstance().addCommands(cmds);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}