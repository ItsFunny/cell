package com.cell.postprocessfactory;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotation.ActivePlugin;
import com.cell.annotation.AutoPlugin;
import com.cell.annotation.CellOrder;
import com.cell.annotation.Exclude;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.collector.AbstractPluginCollector;
import com.cell.config.AbstractInitOnce;
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
import com.cell.utils.CollectionUtils;
import com.cell.utils.ExtensionClassUtil;
import io.netty.util.internal.ConcurrentSet;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
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
public class SpringBeanRegistry extends AbstractInitOnce implements
        PriorityOrdered, BeanDefinitionRegistryPostProcessor
{
    private Map<String, BeanDefinition> pluginBeanDefinitions = new HashMap<>();
    private Map<String, BeanDefinition> factoryDefinition;
    private Map<String, BeanDefinition> postDefinition;
    private static final String defaultPluginPrefixGroup = "activePlugin";
    private static final String factoryBeanPostPrefix = "factoryPost";
    private static final String beanPostPrefix = "post";

    private static final SpringBeanRegistry instance = new SpringBeanRegistry();

    public static SpringBeanRegistry getInstance()
    {
        return instance;
    }

    private SpringBeanRegistry()
    {
    }


    // FIXME ,需要设置beanFactory: 用途: 找到这个bean,或者是提前,将这个注册为bean
    // TODO: 可以写一个manager,专门用于自定义factoryPostProcessor 注册到bean 中

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        List<BeanDefinition> defins = new ArrayList<>(factoryDefinition.values());
        for (BeanDefinition def : defins)
        {
            registry.registerBeanDefinition(factoryBeanPostPrefix + "_" + def.getBeanClassName(), def);
            LOG.info(Module.CONTAINER, "register factory bean {} ", def.getBeanClassName());
        }

        defins = new ArrayList<>(this.postDefinition.values());
        for (BeanDefinition def : defins)
        {
            registry.registerBeanDefinition(beanPostPrefix + "_" + def.getBeanClassName(), def);
            LOG.info(Module.CONTAINER, "register beanPost bean {} ", def.getBeanClassName());
        }

        defins = new ArrayList<>(pluginBeanDefinitions.values());
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
        MultiFilter filter = new MultiFilter();
        Set<Class<?>> activePlugins = ClassUtil.scanPackage(Constants.SCAN_ROOT, filter);
        this.factoryDefinition = new HashMap<>();
        this.postDefinition = new HashMap<>();

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
        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = filter.factories;
        for (Class<? extends IBeanDefinitionRegistryPostProcessorAdapter> clz : factories)
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(clz);
            String beanName = clz.getName();
            this.factoryDefinition.put(beanName, beanDefinition);
            try
            {
                IBeanDefinitionRegistryPostProcessorAdapter adapter = clz.newInstance();
                List<Class<? extends IBeanPostProcessortAdapter>> toRegistryPostProcessor = adapter.getToRegistryPostProcessor();
                if (CollectionUtils.isEmpty(toRegistryPostProcessor))
                {
                    continue;
                }
                for (Class<? extends IBeanPostProcessortAdapter> aClass : toRegistryPostProcessor)
                {
                    GenericBeanDefinition postBeanDef = new GenericBeanDefinition();
                    postBeanDef.setBeanClass(aClass);
                    String postBeanName = beanPostPrefix + "_" + aClass.getName();
                    this.postDefinition.put(postBeanName, postBeanDef);
                }
            } catch (Exception e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }
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

    class MultiFilter implements ClassUtil.ClassFilter
    {
        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = new ConcurrentSet<>();

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

            if (IBeanDefinitionRegistryPostProcessorAdapter.class.isAssignableFrom(clazz) && !AbstractBeanDefiinitionRegistry.class.equals(clazz))
            {
                if (clazz.getAnnotation(Exclude.class) == null)
                {
                    factories.add((Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>) clazz);
                }
            }
            return false;
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
