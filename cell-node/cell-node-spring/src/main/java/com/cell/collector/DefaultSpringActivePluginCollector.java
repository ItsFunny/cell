package com.cell.collector;

import com.cell.annotation.ActivePlugin;
import com.cell.annotation.CellOrder;
import com.cell.annotation.Exclude;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.config.AbstractInitOnce;
import com.cell.constants.Constants;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.utils.ClassUtil;
import com.cell.utils.ExtensionClassUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-24 05:34
 */
@Component
public class DefaultSpringActivePluginCollector extends AbstractInitOnce implements IActivePluginCollector, PriorityOrdered, BeanDefinitionRegistryPostProcessor, BeanFactoryAware
{
    private Set<Class<?>> activePlugins;
    private Map<String, BeanDefinition> pluginBeanDefinitions = new HashMap<>();
    private final String defaultBeanNamePrefix = "activePlugin.";

    @Override
    public <T> List<T> getPlugins(Class<T> superclz)
    {
        return null;
    }

    @Override
    public <A extends Annotation, T> List<T> getPlugins(Class<T> superclz, Class<A> ano)
    {
        return null;
    }

    @Override
    public <A extends Annotation> List<Object> getPluginsByAnno(Class<A> ano)
    {
        return null;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException
    {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException
    {

    }

    @Override
    public int getOrder()
    {
        return 0;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.activePlugins = ClassUtil.scanPackage(Constants.SCAN_ROOT, new AutoClassFilter());
        for (Class<?> clz : activePlugins)
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(clz);
            if (ISpringNodeExtension.class.isAssignableFrom(clz))
            {
                processExtension(beanDefinition);
            }
            String beanName = defaultBeanNamePrefix + clz.getName();
            beanDefinition.setAttribute(SpringBridge.BEAN_NAME_ATTR, beanName);
            pluginBeanDefinitions.put(beanName, beanDefinition);
            LOG.info(Module.CONTAINER, "add {} beandefinition", clz);
        }
        LOG.info(Module.CONTAINER, "DefaultActivePluginCollector init success");
    }

    private void processExtension(GenericBeanDefinition beanDefinition)
    {
        beanDefinition.setAttribute(SpringBridge.EXTENSION_FLAG_ATTR, true);
        CellOrder annotation = beanDefinition.getBeanClass().getAnnotation(CellOrder.class);
        int order = Constants.EXTESNION_MIN_NUM_ORDER;
        if (null != annotation)
        {
            order = annotation.value();
        }
        beanDefinition.setAttribute(ExtensionClassUtil.ORDER_ATTRIBUTE, order);

    }

    public static class AutoClassFilter implements ClassUtil.ClassFilter
    {
        @Override
        public boolean accept(Class<?> clazz)
        {
//            if(clazz.isInterface() || clazz.equals(AbstractNodeExtension.class)) {
            if (clazz.isInterface())
            {
                return false;
            }
            ActivePlugin anno = clazz.getAnnotation(ActivePlugin.class);
            if (anno != null)
            {
                return true;
            }
            if (ISpringNodeExtension.class.isAssignableFrom(clazz))
            {
                Exclude exclude = clazz.getAnnotation(Exclude.class);
                if (exclude != null)
                {
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}
