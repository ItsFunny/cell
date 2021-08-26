package com.cell.postprocessfactory;

import com.cell.annotation.ActivePlugin;
import com.cell.annotation.AutoPlugin;
import com.cell.annotation.CellOrder;
import com.cell.annotation.Exclude;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.collector.AbstractPluginCollector;
import com.cell.constants.Constants;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.extension.AbstractNodeExtension;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.postprocessors.dependency.SpringBeanDependenciesPostProcessor;
import com.cell.postprocessors.extension.SpringExtensionManager;
import com.cell.utils.ClassUtil;
import com.cell.utils.ExtensionClassUtil;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-24 05:34
 */
@Data
public class DefaultSpringActivePluginCollector extends AbstractPluginCollector implements
        PriorityOrdered, BeanDefinitionRegistryPostProcessor, BeanFactoryAware
{
    private BeanFactory factory;
    private Map<String, BeanDefinition> pluginBeanDefinitions = new HashMap<>();
    private AutoPluginResolver resolver;

    private DefaultSpringActivePluginCollector()
    {
        this.initOnce(null);
    }


    @Override
    protected <T> T getInstance(Class<?> clz)
    {
        T b = (T) factory.getBean(clz);
        return b;
    }

    // FIXME ,需要设置beanFactory: 用途: 找到这个bean,或者是提前,将这个注册为bean
    // TODO: 可以写一个manager,专门用于自定义factoryPostProcessor 注册到bean 中
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.factory = beanFactory;
        AutowiredAnnotationBeanPostProcessor processor = this.factory.getBean(AutowiredAnnotationBeanPostProcessor.class);
        processor.setAutowiredAnnotationTypes(this.resolver.getAnnotations());

        try
        {
//            SpringBeanDependenciesPostProcessor processor2 = this.factory.getBean(SpringBeanDependenciesPostProcessor.class);
//            processor2.setAutowiredAnnotationTypes(this.resolver.getAnnotations());
            SpringBeanDependenciesPostProcessor.getInstance().setAutowiredAnnotationTypes(this.resolver.getAnnotations());
        } catch (Throwable e)
        {

        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        List<BeanDefinition> defins = new ArrayList<>(pluginBeanDefinitions.values());
        Collections.sort(defins, new BeanDefListComparator());

        // 注册beanDefinition
        for (BeanDefinition def : defins)
        {
            Class<?> clz = ((GenericBeanDefinition) def).getBeanClass();
            if (ISpringNodeExtension.class.isAssignableFrom(clz))
            {
                registry.registerBeanDefinition((String) def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
                LOG.info(Module.CONTAINER_REGISTRY, "register node extension name %s and beanDefination %s", def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
            } else
            {
                registry.registerBeanDefinition((String) def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
                LOG.info(Module.CONTAINER_REGISTRY, "register beanName %s and beanDefination %s", def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException
    {
        configurableListableBeanFactory.addBeanPostProcessor(SpringExtensionManager.getInstance());
    }

    @Override
    public int getOrder()
    {
        return SpringBridge.BEAN_REGISTER_ORDERER + 1;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.resolver = new AutoPluginResolver();
        this.activePlugins = ClassUtil.scanPackage(Constants.SCAN_ROOT, new AutoClassFilter());
        for (Class<?> clz : activePlugins)
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(clz);
            if (ISpringNodeExtension.class.isAssignableFrom(clz))
            {
                processExtension(beanDefinition);
            }
            String beanName = this.defaultPluginPrefixGroup + clz.getName();
            beanDefinition.setAttribute(SpringBridge.BEAN_NAME_ATTR, beanName);
            pluginBeanDefinitions.put(beanName, beanDefinition);
            LOG.info(Module.CONTAINER, "add {} beandefinition", clz);
        }
        LOG.info(Module.CONTAINER, "DefaultActivePluginCollector init success");
        SpringExtensionManager.getInstance().setBeanDefinitionMap(pluginBeanDefinitions);
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
            if (clazz.isInterface() || clazz.equals(AbstractNodeExtension.class) || clazz.equals(AbstractSpringNodeExtension.class))
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

    public static class BeanDefListComparator implements Comparator<BeanDefinition>
    {

        @Override
        public int compare(BeanDefinition o1, BeanDefinition o2)
        {
            Class<?> clz1 = ((GenericBeanDefinition) o1).getBeanClass();
            Class<?> clz2 = ((GenericBeanDefinition) o2).getBeanClass();
            return ClassUtil.ordererCompare(clz1, clz2);
        }

    }
}
