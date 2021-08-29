package com.cell.postprocessor;

import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.AutoPlugin;
import com.cell.collector.AbstractPluginCollector;
import com.cell.context.InitCTX;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.postprocessors.dependency.SpringBeanDependenciesPostProcessor;
import com.cell.postprocessors.extension.SpringExtensionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 03:20
 */
public class SpringPluginCollectorFactoryProcessor extends AbstractPluginCollector implements
        PriorityOrdered, IBeanDefinitionRegistryPostProcessorAdapter, BeanFactoryAware
{
    private AutoPluginResolver resolver;

    public SpringPluginCollectorFactoryProcessor()
    {
        this.resolver = new AutoPluginResolver();
    }


    private BeanFactory factory;

    public static class AutoPluginResolver
    {
        private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();

        @SuppressWarnings("unchecked")
        public AutoPluginResolver()
        {
            this.autowiredAnnotationTypes.add(Autowired.class);
            this.autowiredAnnotationTypes.add(Value.class);
            this.autowiredAnnotationTypes.add(AutoPlugin.class);
            try
            {
                this.autowiredAnnotationTypes.add((Class<? extends Annotation>)
                        ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
                LOG.info(Module.CONTAINER_REGISTRY, "JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
            } catch (ClassNotFoundException ex)
            {
                // JSR-330 API not available - simply skip.
            }
        }

        public Set<Class<? extends Annotation>> getAnnotations()
        {
            return this.autowiredAnnotationTypes;
        }
    }

    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return null;
    }

    @Override
    protected <T> T getInstance(Class<?> clz)
    {
        T b = (T) factory.getBean(clz);
        return b;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException
    {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        factory.addBeanPostProcessor(SpringExtensionManager.getInstance());
    }

    @Override
    public int getOrder()
    {
        return 0;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.factory = beanFactory;
        AutowiredAnnotationBeanPostProcessor processor = this.factory.getBean(AutowiredAnnotationBeanPostProcessor.class);
        processor.setAutowiredAnnotationTypes(this.resolver.getAnnotations());

        try
        {
            SpringBeanDependenciesPostProcessor processor2 = this.factory.getBean(SpringBeanDependenciesPostProcessor.class);
            processor2.setAutowiredAnnotationTypes(this.resolver.getAnnotations());
        } catch (Throwable e)
        {

        }
    }
}
