package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.ActiveConfiguration;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.CellOrder;
import com.cell.annotations.LifeCycle;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.config.AbstractInitOnce;
import com.cell.config.ConfigConstants;
import com.cell.constants.Constants;
import com.cell.constants.OrderConstants;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.enums.EnumLifeCycle;
import com.cell.log.LOG;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.postprocessors.extension.SpringExtensionManager;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ExtensionClassUtil;
import com.cell.utils.StringUtils;
import com.cell.wrapper.AnnotaionManagerWrapper;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.PriorityOrdered;

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
@LifeCycle(lifeCycle = EnumLifeCycle.ONCE)
public class SpringBeanRegistry extends AbstractBeanDefiinitionRegistry implements
        PriorityOrdered
{
    //    private Map<String, BeanDefinition> extensionBeanDefinitions = new HashMap<>();
    private Map<String, BeanDefinition> pluginBeanDefinitions = new HashMap<>();
    private Map<String, BeanDefinition> factoryDefinition;
    private Map<String, BeanDefinition> postDefinition;
    private Map<String, BeanDefinition> cfgDefinition;


    // 用于

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
        // FIXME which means ,maybe it is pure springboot
        List<BeanDefinition> defins = new ArrayList<>(factoryDefinition.values());
        for (BeanDefinition def : defins)
        {
            registry.registerBeanDefinition(SpringBridge.factoryBeanPostPrefix + "_" + def.getBeanClassName(), def);
            LOG.info(Module.CONTAINER, "register factory bean {} ", def.getBeanClassName());
        }

        defins = new ArrayList<>(this.postDefinition.values());
        for (BeanDefinition def : defins)
        {
            registry.registerBeanDefinition(SpringBridge.beanPostPrefix + "_" + def.getBeanClassName(), def);
            LOG.info(Module.CONTAINER, "register beanPost bean {} ", def.getBeanClassName());
        }

        if (!StringUtils.isEmpty(cfgDefinition))
        {
            defins = new ArrayList<>(cfgDefinition.values());
            for (BeanDefinition def : defins)
            {
                registry.registerBeanDefinition((String) def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
            }
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
                LOG.info(Module.CONTAINER_REGISTRY, "register node extension name {} and beanDefination {}", def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
            } else
            {
                registry.registerBeanDefinition((String) def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
                LOG.info(Module.CONTAINER_REGISTRY, "register beanName {} and beanDefination {}", def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
            }
        }
    }

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {

    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        factory.addBeanPostProcessor(SpringExtensionManager.getInstance());
    }


    @Override
    public int getOrder()
    {
        return SpringBridge.BEAN_REGISTER_ORDERER + 1;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.factoryDefinition = new HashMap<>();
        this.postDefinition = new HashMap<>();
        Set<Class<?>> activePlugins = (Set<Class<?>>) ctx.getData().get(ConfigConstants.ACTIVE_PLUGINS);
        for (Class<?> clz : activePlugins)
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(clz);
            String group = SpringBridge.defaultExtensionGroup;
            Map<String, BeanDefinition> definitionMap = this.pluginBeanDefinitions;
            if (ISpringNodeExtension.class.isAssignableFrom(clz))
            {
                processExtension(beanDefinition);
            } else
            {
//                group = SpringBridge.defaultPluginPrefixGroup;
//                definitionMap = this.pluginBeanDefinitions;
            }
            String beanName = group + clz.getName();
            beanDefinition.setAttribute(SpringBridge.BEAN_NAME_ATTR, beanName);
            definitionMap.put(beanName, beanDefinition);

            LOG.info(Module.CONTAINER, "add {} beandefinition", clz);
        }

        Set<Class<?>> configurationClass = (Set<Class<?>>) ctx.getData().get(ConfigConstants.configurationClasses);
        if (!CollectionUtils.isEmpty(configurationClass))
        {
            this.cfgDefinition = new HashMap<>();
            for (Class<?> aClass : configurationClass)
            {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(aClass);
                String beanName = SpringBridge.defaultCfgGroup + aClass.getName();
                beanDefinition.setAttribute(SpringBridge.BEAN_NAME_ATTR, beanName);
                beanDefinition.setAttribute(SpringBridge.ACTIVE_CFG_ATTR, true);
                this.cfgDefinition.put(beanName, beanDefinition);
            }
        }

        Set<Class<? extends IBeanPostProcessortAdapter>> prepareToRegistryPostProcessor = (Set<Class<? extends IBeanPostProcessortAdapter>>) ctx.getData().get(ConfigConstants.toRegistryPostProcessor);
        this.registerPostProcessors(prepareToRegistryPostProcessor);

        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = (Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>>) ctx.getData().get(ConfigConstants.FACTORIES);
        Map<Class<? extends Annotation>, List<Class<?>>> interestAnnotationsClazzs = (Map<Class<? extends Annotation>, List<Class<?>>>) ctx.getData().get(ConfigConstants.interestAnnotationsClazzs);
        for (Class<? extends IBeanDefinitionRegistryPostProcessorAdapter> clz : factories)
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(clz);
            String beanName = clz.getName();
            this.factoryDefinition.put(beanName, beanDefinition);
            try
            {
                IBeanDefinitionRegistryPostProcessorAdapter adapter = clz.newInstance();
                adapter.choseInterestAnnotations(interestAnnotationsClazzs);
                List<Class<? extends IBeanPostProcessortAdapter>> toRegistryPostProcessor = adapter.getToRegistryPostProcessor();
                if (CollectionUtils.isEmpty(toRegistryPostProcessor))
                {
                    continue;
                }
                this.registerPostProcessors(new HashSet<>(toRegistryPostProcessor));
            } catch (Exception e)
            {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        LOG.info(Module.CONTAINER, "DefaultActivePluginCollector init success");
        Map<String, AnnotaionManagerWrapper> managers = (Map<String, AnnotaionManagerWrapper>) ctx.getData().get(ConfigConstants.MANAGERS);

        SpringExtensionManager.getInstance().setBeanDefinitionMap(pluginBeanDefinitions);
        SpringExtensionManager.getInstance().setManagers(managers);
    }

    private void registerPostProcessors(Set<Class<? extends IBeanPostProcessortAdapter>> prepareToRegistryPostProcessor)
    {
        if (CollectionUtils.isEmpty(prepareToRegistryPostProcessor)) return;
        for (Class<? extends IBeanPostProcessortAdapter> aClass : prepareToRegistryPostProcessor)
        {
            GenericBeanDefinition postBeanDef = new GenericBeanDefinition();
            postBeanDef.setBeanClass(aClass);
            String postBeanName = SpringBridge.beanPostPrefix + "_" + aClass.getName();
            this.postDefinition.put(postBeanName, postBeanDef);
        }
    }


    private void processExtension(GenericBeanDefinition beanDefinition)
    {
        beanDefinition.setAttribute(SpringBridge.EXTENSION_FLAG_ATTR, true);
        CellOrder annotation = beanDefinition.getBeanClass().getAnnotation(CellOrder.class);
        int order = OrderConstants.EXTESNION_MIN_NUM_ORDER;
        if (null != annotation)
        {
            order = annotation.value();
        }
        beanDefinition.setAttribute(ExtensionClassUtil.ORDER_ATTRIBUTE, order);
    }

    private void processActivePlugin(GenericBeanDefinition beanDefinition)
    {

    }


    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return null;
    }

    @Override
    public void choseInterestAnnotations(Map<Class<? extends Annotation>, List<Class<?>>> classListMap)
    {

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
