package com.cell.initializer;

import com.cell.App;
import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.annotations.*;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.comparators.OrderComparator;
import com.cell.config.AbstractInitOnce;
import com.cell.config.Config;
import com.cell.config.ConfigConstants;
import com.cell.constants.Constants;
import com.cell.context.InitCTX;
import com.cell.enums.EnumLifeCycle;
import com.cell.extension.AbstractNodeExtension;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.manager.IManagerFactory;
import com.cell.manager.IManagerNode;
import com.cell.manager.IManagerNodeFactory;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.postprocessor.SpringBeanRegistry;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ReflectUtil;
import com.cell.wrapper.AnnotaionManagerWrapper;
import com.cell.wrapper.AnnotationNodeWrapper;
import io.netty.util.internal.ConcurrentSet;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-22 18:00
 */
public class SpringInitializer extends AbstractInitOnce implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
    private List<IBeanDefinitionRegistryPostProcessorAdapter> processors = new ArrayList<>();


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {
        this.initOnce(new InitCTX());
        applicationContext.addBeanFactoryPostProcessor(SpringBeanRegistry.getInstance());
        for (IBeanDefinitionRegistryPostProcessorAdapter processor : processors)
        {
            applicationContext.addBeanFactoryPostProcessor(processor);
        }
        this.processors = null;
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


        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = filter.factories;
        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> dropOffFactories = new HashSet<>();
        Set<Class<? extends IBeanPostProcessortAdapter>> toRegistryPostProcessor = new HashSet<>();
        for (Class<? extends IBeanDefinitionRegistryPostProcessorAdapter> clz : factories)
        {
            LifeCycle anno = clz.getAnnotation(LifeCycle.class);
            if (anno != null && anno.lifeCycle() == EnumLifeCycle.ONCE)
            {
                IBeanDefinitionRegistryPostProcessorAdapter adapter = (IBeanDefinitionRegistryPostProcessorAdapter) ReflectUtil.newInstance(clz);
                adapter.choseInterestAnnotations(filter.interestAnnotationsClazzs);
                List<Class<? extends IBeanPostProcessortAdapter>> postProcessors = adapter.getToRegistryPostProcessor();
                if (!CollectionUtils.isEmpty(postProcessors))
                {
                    toRegistryPostProcessor.addAll(postProcessors);
                }

                processors.add(adapter);
                continue;
            }
            dropOffFactories.add(clz);
        }


        ctx.getData().put(ConfigConstants.ACTIVE_PLUGINS, activePlugins);
        ctx.getData().put(ConfigConstants.FACTORIES, dropOffFactories);
        ctx.getData().put(ConfigConstants.MANAGERS, filter.managers);
        ctx.getData().put(ConfigConstants.interestAnnotationsClazzs, filter.interestAnnotationsClazzs);
        ctx.getData().put(ConfigConstants.toRegistryPostProcessor, toRegistryPostProcessor);
        SpringBeanRegistry.getInstance().initOnce(ctx);

        for (IBeanDefinitionRegistryPostProcessorAdapter adapter : processors)
        {
            adapter.initOnce(ctx);
        }
    }


    class MultiFilter implements ClassUtil.ClassFilter
    {
        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = new ConcurrentSet<>();
        final Map<String, AnnotaionManagerWrapper> managers = new ConcurrentHashMap<>();

        final Set<Class<? extends IManagerNodeFactory>> nodeFactories = new ConcurrentSet<>();
        final Map<String, List<AnnotationNodeWrapper>> annotationNodes = new HashMap<>();

        final Set<Class<?>> configInterestClasses = new HashSet<>();
        final Set<Class<? extends Annotation>> interestAnnotations = new HashSet<>(Arrays.asList(ReactorAnno.class));


        final Map<Class<? extends Annotation>, List<Class<?>>> interestAnnotationsClazzs = new ConcurrentHashMap<>();

        public MultiFilter()
        {
            Set<Class<? extends Annotation>> interestAnnotations = Config.getInterestAnnotations();
            this.interestAnnotations.addAll(interestAnnotations);
            this.configInterestClasses.addAll(Config.getInterestClasses());
        }


        // FIXME ,太傻了这种写法
        @Override
        public boolean accept(Class<?> clazz)
        {
            String s = clazz.getName().toLowerCase();
            if (clazz.isInterface() || clazz.equals(AbstractNodeExtension.class)
                    || clazz.equals(AbstractSpringNodeExtension.class) || s.startsWith("abs")
                    // 特殊指定该类,不对其进行特殊处理
                    || clazz.equals(SpringBeanRegistry.class))
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
        // FIXME ,可能会出现,既有 interestClass,又有被注解所标识的类
        private void handleIsSpecial(Class<?> clazz)
        {
            if (!this.configInterestClasses.contains(clazz))
            {

            }
            for (Class<? extends Annotation> c : this.interestAnnotations)
            {
                if (clazz.getAnnotation(c) != null)
                {
                    synchronized (this.interestAnnotationsClazzs)
                    {
                        List<Class<?>> classes = this.interestAnnotationsClazzs.get(c);
                        if (CollectionUtils.isEmpty(classes))
                        {
                            classes = new ArrayList<>();
                            this.interestAnnotationsClazzs.put(c, classes);
                        }
                        classes.add(clazz);
                    }
                }
            }

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
                            wrapper.setManager(instance);
                        }
                    } else
                    {
                        wrapper = new AnnotaionManagerWrapper();
                        wrapper.setManager(instance);
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
                boolean asBean = managerNode.lifeCycle() == EnumLifeCycle.ONCE;
                synchronized (this.annotationNodes)
                {
                    List<AnnotationNodeWrapper> objects = this.annotationNodes.get(group);
                    boolean contains = !CollectionUtils.isEmpty(objects);
                    if (!contains)
                    {
                        objects = new ArrayList<>();
                        this.annotationNodes.put(group, objects);
                    }
                    AnnotationNodeWrapper wp = new AnnotationNodeWrapper(node, asBean);
                    objects.add(wp);
                    LOG.info(Module.CONTAINER, "nodeManger添加 被注解所包裹的node,group:{},node:{}", group, name);
                }
            } catch (Exception e)
            {
                LOG.warning(Module.CONTAINER, e, "实例化失败:{}", e.getMessage());
            }
        }
    }

    private void firstAfterScan(MultiFilter filter) throws Exception
    {
        List<Class<? extends IManagerNodeFactory>> factories = new ArrayList<>(filter.nodeFactories);
        Collections.sort(factories, new OrderComparator());
        Set<String> keys = filter.managers.keySet();

        Map<String, List<AnnotationNodeWrapper>> annotationNodes = new HashMap<>(filter.annotationNodes);
        for (String key : keys)
        {
            if (annotationNodes.containsKey(key)) continue;
            annotationNodes.put(key, new ArrayList<>());
        }

        for (Class<? extends IManagerNodeFactory> factory : factories)
        {
            IManagerNodeFactory nodeFactory = factory.newInstance();
            IManagerNode node = nodeFactory.createNode();
            List<AnnotationNodeWrapper> objects = annotationNodes.get(node.group());
            if (CollectionUtils.isEmpty(objects)) continue;
            objects.add(new AnnotationNodeWrapper(node, false));
        }
        for (String key : annotationNodes.keySet())
        {
            List<AnnotationNodeWrapper> objects = annotationNodes.get(key);
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
            for (AnnotationNodeWrapper wp : objects)
            {
                Object object = wp.getNode();
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

                boolean contains = annotaionManagerWrapper.getManagerNodes().containsKey(name);
                if (contains)
                {
                    LOG.warn(Module.CONTAINER, "重复的node,group:{},name:{}", group, name);
                }
                if (!contains || override)
                {
                    annotaionManagerWrapper.getManagerNodes().put(name, wp);
                }
            }
        }
    }
}
