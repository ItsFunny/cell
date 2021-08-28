package com.cell.postprocessfactory;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotation.*;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.comparators.OrderComparator;
import com.cell.config.AbstractInitOnce;
import com.cell.constants.Constants;
import com.cell.constants.SpringBridge;
import com.cell.context.InitCTX;
import com.cell.extension.AbstractNodeExtension;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.manager.IManagerFactory;
import com.cell.manager.IManagerNode;
import com.cell.manager.IManagerNodeFactory;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.postprocessors.extension.SpringExtensionManager;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ExtensionClassUtil;
import io.netty.util.internal.ConcurrentSet;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.PriorityOrdered;
import org.springframework.format.support.FormatterPropertyEditorAdapter;
import sun.reflect.generics.scope.ClassScope;
import sun.reflect.misc.ReflectUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
                LOG.info(Module.CONTAINER_REGISTRY, "register node extension name {} and beanDefination {}", def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
            } else
            {
                registry.registerBeanDefinition((String) def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
                LOG.info(Module.CONTAINER_REGISTRY, "register beanName {} and beanDefination {}", def.getAttribute(SpringBridge.BEAN_NAME_ATTR), def);
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
        try
        {
            this.firstAfterScan(filter);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
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

        try
        {
            this.finalHandleManager(filter.managers);
        } catch (Exception e)
        {
            // FIXME
            throw new RuntimeException(e);
        }

        SpringExtensionManager.getInstance().setBeanDefinitionMap(pluginBeanDefinitions);
        SpringDependecyFactoryProcessor.getInstance().initOnce(ctx);
        ExtensionClassFactoryProcessor.getInstance().initOnce(ctx);
    }


    private void firstAfterScan(MultiFilter filter) throws Exception
    {
        List<Class<? extends IManagerNodeFactory>> factories = new ArrayList<>(filter.nodeFactories);
        Collections.sort(factories, new OrderComparator());
        Set<String> keys = filter.managers.keySet();

        Map<String, List<Object>> annotationNodes = new HashMap<>(filter.annotationNodes);
        for (String key : keys)
        {
            if (annotationNodes.containsKey(key)) continue;
            annotationNodes.put(key, new ArrayList<>());
        }

        for (Class<? extends IManagerNodeFactory> factory : factories)
        {
            IManagerNodeFactory nodeFactory = factory.newInstance();
            IManagerNode node = nodeFactory.createNode();
            List<Object> objects = annotationNodes.get(node.group());
            if (CollectionUtils.isEmpty(objects)) continue;
            objects.add(node);
        }
        for (String key : annotationNodes.keySet())
        {
            List<Object> objects = annotationNodes.get(key);
            if (CollectionUtils.isEmpty(objects)) continue;
            Collections.sort(objects, (o1, o2) -> ClassUtil.ordererCompare(o1.getClass(), o2.getClass()));
            AnnotaionManagerWrapper annotaionManagerWrapper = filter.managers.get(key);
            if (annotaionManagerWrapper == null)
            {
                continue;
            }
            String name = null;
            String group = null;
            boolean override = false;
            for (Object object : objects)
            {
                if (object instanceof IManagerNode)
                {
                    name = ((IManagerNode) object).name();
                    group = ((IManagerNode) object).group();
                    override = ((IManagerNode) object).override();
                } else
                {
                    name = object.getClass().getAnnotation(ManagerNode.class).name();
                    group = object.getClass().getAnnotation(ManagerNode.class).group();
                    override = object.getClass().getAnnotation(ManagerNode.class).override();
                }

                boolean contains = annotaionManagerWrapper.managerNodes.containsKey(name);
                if (contains)
                {
                    LOG.warn(Module.CONTAINER, "重复的node,group:{},name:{}", group, name);
                }
                if (!contains || override)
                {
                    annotaionManagerWrapper.managerNodes.put(name, object);
                }
            }

        }
//        AnnotaionManagerWrapper annotaionManagerWrapper = filter.managers.get(node.group());
//        if (annotaionManagerWrapper == null)
//        {
//            continue;
//        }
//
//        boolean contains = annotaionManagerWrapper.managerNodes.containsKey(node.name());
//        if (contains)
//        {
//            LOG.warn(Module.CONTAINER, "重复的node,group:{},name:{}", node.group(), node.name());
//        }
//        if (!contains || node.override())
//        {
//            annotaionManagerWrapper.managerNodes.put(node.name(), node);
//        }
    }

    private void finalHandleManager(Map<String, AnnotaionManagerWrapper> managers) throws Exception
    {
        Set<String> keys = managers.keySet();
        for (String key : keys)
        {
            AnnotaionManagerWrapper wrapper = managers.get(key);
            IReflectManager manager = wrapper.manager;
            manager.invokeInterestNodes(wrapper.managerNodes.values());
        }
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

    class AnnotaionManagerWrapper
    {
        IReflectManager manager;
        Map<String, Object> managerNodes = new ConcurrentHashMap<>();
    }

    class MultiFilter implements ClassUtil.ClassFilter
    {

        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = new ConcurrentSet<>();
        final Map<String, AnnotaionManagerWrapper> managers = new ConcurrentHashMap<>();

        final Set<Class<? extends IManagerNodeFactory>> nodeFactories = new ConcurrentSet<>();
        final Map<String, List<Object>> annotationNodes = new HashMap<>();

        // FIXME ,太傻了这种写法
        @Override
        public boolean accept(Class<?> clazz)
        {
            String s = clazz.getName().toLowerCase();
            if (clazz.isInterface() || clazz.equals(AbstractNodeExtension.class) || clazz.equals(AbstractSpringNodeExtension.class) || s.startsWith("abs"))
            {
                return false;
            }
            ActivePlugin anno = clazz.getAnnotation(ActivePlugin.class);
            if (anno != null)
            {
                // FIXME ,可能同时是IManager,或者是IManagerNode
                this.handleIsSpecial(clazz);
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
            this.handleIsSpecial(clazz);
            return false;
        }

        // 只扫描2种factory的类
        private void handleIsSpecial(Class<?> clazz)
        {
            if (IManagerFactory.class.isAssignableFrom(clazz))
            {
                IManagerFactory factory = null;
                try
                {
                    factory = (IManagerFactory) clazz.newInstance();
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                IReflectManager instance = factory.createInstance();
                String name = instance.name();
                synchronized (this.managers)
                {
                    AnnotaionManagerWrapper wrapper = this.managers.get(name);
                    if (wrapper != null)
                    {
                        LOG.warn(Module.CONTAINER, "重复的managerName,{}", name);
                        if (instance.override())
                        {
                            wrapper.manager = instance;
                        }
                    } else
                    {
                        wrapper = new AnnotaionManagerWrapper();
                        wrapper.manager = instance;
                        this.managers.put(name, wrapper);
                    }
                }
                return;
            }

            ManagerNode managerNode = clazz.getAnnotation(ManagerNode.class);
            if (managerNode == null && !IManagerNodeFactory.class.isAssignableFrom(clazz))
            {
                return;
            }

            if (managerNode == null)
            {
                this.nodeFactories.add((Class<? extends IManagerNodeFactory>) clazz);
                return;
            }

            try
            {
                Object node = ReflectUtil.newInstance(clazz);
                String group = managerNode.group();
                String name = managerNode.name();
                synchronized (this.annotationNodes)
                {
                    List<Object> objects = this.annotationNodes.get(group);
                    boolean contains = !CollectionUtils.isEmpty(objects);
                    if (!contains)
                    {
                        objects = new ArrayList<>();
                        this.annotationNodes.put(group,objects);
                    }
                    objects.add(node);
                    LOG.info(Module.CONTAINER, "nodeManger添加 被注解所包裹的node,group:{},node:{}", group, name);
                }
            } catch (Exception e)
            {
                LOG.warning(Module.CONTAINER, e, "实例化失败:{}", e.getMessage());
            }
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
