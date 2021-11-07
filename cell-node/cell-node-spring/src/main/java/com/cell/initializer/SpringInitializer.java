package com.cell.initializer;

import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.rpc.grpc.client.framework.annotation.CellSpringHttpApplication;
import com.cell.annotations.*;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.http.gate.config.AbstractInitOnce;
import com.cell.http.gate.config.Config;
import com.cell.http.gate.config.ConfigConstants;
import com.cell.constants.BitConstants;
import com.cell.constants.Constants;
import com.cell.context.InitCTX;
import com.cell.enums.EnumLifeCycle;
import com.cell.exceptions.ProgramaException;
import com.cell.discovery.nacos.http.extension.AbstractNodeExtension;
import com.cell.discovery.nacos.http.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.postprocessor.ManagerFactoryPostProcessor;
import com.cell.postprocessor.SpringBeanRegistry;
import com.cell.rpc.grpc.client.framework.util.FrameworkUtil;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ReflectUtil;
import com.cell.utils.ReflectionUtils;
import com.cell.wrapper.AnnotaionManagerWrapper;
import com.cell.wrapper.AnnotationNodeWrapper;
import io.netty.util.internal.ConcurrentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

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
        applicationContext.addBeanFactoryPostProcessor(ManagerFactoryPostProcessor.getInstance());
        for (IBeanDefinitionRegistryPostProcessorAdapter processor : processors)
        {
            applicationContext.addBeanFactoryPostProcessor(processor);
        }
        this.processors = null;
    }


    @Override
    protected void onInit(InitCTX ctx)
    {
        Collection<CellSpringInitializer> initializers = FrameworkUtil.getSpringFactoriesInstances(CellSpringInitializer.class, new Class<?>[]{});
        Set<Class<? extends Annotation>> interesetAnnotations = new HashSet<>();
        for (CellSpringInitializer initializer : initializers)
        {
            Class<? extends Annotation>[] interestAnnotation = initializer.getInterestAnnotation();
            interesetAnnotations.addAll(Arrays.asList(interestAnnotation));
        }


        Class<?> mainApplicationClass = ClassUtil.getMainApplicationClass();
        CellSpringHttpApplication mergedAnnotation = ClassUtil.getMergedAnnotation(mainApplicationClass, CellSpringHttpApplication.class);
        String rootPath = Constants.SCAN_ROOT;
        if (mergedAnnotation != null)
        {
            String[] scans = mergedAnnotation.scanBasePackages();
            rootPath = scans[0];
        }

        Class<? extends AbstractNodeExtension>[] excludeNodeExtensions = mergedAnnotation.scanExcludeNodeExtensions();
        Class<? extends Annotation>[] interestAnnotations = mergedAnnotation.scanInterestAnnotations();
        interesetAnnotations.addAll(Arrays.asList(interestAnnotations));
        Class<?>[] excludeClasses = mergedAnnotation.scanExcludeClasses();


        MultiFilter filter = new MultiFilter();
        filter.excludeClasses.addAll(Arrays.asList(excludeClasses));
        filter.excludeNodeExtensions.addAll(Arrays.asList(excludeNodeExtensions));
        filter.interestAnnotations.addAll(interesetAnnotations);

        // FIXME ,需要重构该部分,使用reflections
        Set<Class<?>> activePlugins = ClassUtil.scanPackage(rootPath, filter);


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

        ctx.getData().put(ConfigConstants.annotationNodes, filter.annotationNodes);
        ctx.getData().put(ConfigConstants.MANAGERS, filter.managers);
        ctx.getData().put(ConfigConstants.ACTIVE_PLUGINS, activePlugins);
        ctx.getData().put(ConfigConstants.FACTORIES, dropOffFactories);
        ctx.getData().put(ConfigConstants.interestAnnotationsClazzs, filter.interestAnnotationsClazzs);
        ctx.getData().put(ConfigConstants.toRegistryPostProcessor, toRegistryPostProcessor);
        ctx.getData().put(ConfigConstants.configurationClasses, filter.configurationClass);

        ManagerFactoryPostProcessor.getInstance().initOnce(ctx);
        SpringBeanRegistry.getInstance().initOnce(ctx);

        for (IBeanDefinitionRegistryPostProcessorAdapter adapter : processors)
        {
            adapter.initOnce(ctx);
        }
    }


    // FIXME ,重复的class 处理: 既 既有@ActivePlugin 又是interest
    class MultiFilter implements ClassUtil.ClassFilter
    {
        // config
        final Set<Class<?>> configInterestClasses = new HashSet<>();
        final Set<Class<? extends Annotation>> interestAnnotations = new HashSet<>();
        final Set<Class<? extends Annotation>> excludeAnnotations = new HashSet<>(Arrays.asList(Exclude.class));
        final Set<Class<? extends AbstractNodeExtension>> excludeNodeExtensions = new HashSet<>();
        final Set<Class<?>> excludeClasses = new HashSet<>();

        // cfg
        final Set<Class<?>> configurationClass = new HashSet<>();

        // flag
        final Set<Class<?>> set = new HashSet<>();

        // data
        Set<Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>> factories = new ConcurrentSet<>();
        final Map<String, AnnotaionManagerWrapper> managers = new ConcurrentHashMap<>();

        final Map<String, List<AnnotationNodeWrapper>> annotationNodes = new HashMap<>();

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

            boolean ret = false;
            try
            {
                if (this.excludeClasses.contains(clazz))
                {
                    LOG.info(Module.CONTAINER, "exclude class,{}", clazz);
                    return false;
                }
                if (excludeAnnotations.contains(clazz))
                {
                    LOG.info(Module.CONTAINER, "exlude Annotation,{}", clazz);
                    return false;
                }
                String s = clazz.getName().toLowerCase();
                // TODO OPTIMIZE
                if (clazz.isInterface() || clazz.equals(AbstractNodeExtension.class)
                        || clazz.equals(AbstractSpringNodeExtension.class) || s.startsWith("abs")
                        // 特殊指定该类,不对其进行特殊处理
                        || clazz.equals(SpringBeanRegistry.class) || ClassUtil.checkIsAbstract(clazz))
                {
                    ret = false;
                    return ret;
                }
                ActiveConfiguration cfg = ClassUtil.getMergedAnnotation(clazz, ActiveConfiguration.class);
                if (cfg != null)
                {
                    synchronized (this.configurationClass)
                    {
                        this.configurationClass.add(clazz);
                    }
                    return false;
                }
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(clazz, ActivePlugin.class);
                if (attributes != null && !attributes.isEmpty())
                {
                    // FIXME ,可能同时是IManager,或者是IManagerNode
                    this.handleIsSpecial(clazz);
                    ret = true;
                    return ret;
                }
                if (ISpringNodeExtension.class.isAssignableFrom(clazz))
                {
                    if (excludeNodeExtensions.contains(clazz))
                    {
                        LOG.info(Module.CONTAINER, "exclude nodeExtension,{}", clazz);
                        return false;
                    }
                    ret = true;
                    return ret;
                }

                if (IBeanDefinitionRegistryPostProcessorAdapter.class.isAssignableFrom(clazz) && !ClassUtil.checkIsAbstract(clazz))
                {
                    if (clazz.getAnnotation(Exclude.class) == null)
                    {
                        factories.add((Class<? extends IBeanDefinitionRegistryPostProcessorAdapter>) clazz);
                    }
                }
                this.handleIsSpecial(clazz);
                ret = false;
                return false;
            } finally
            {
            }

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
                Annotation mergedAnnotation = ClassUtil.getMergedAnnotation(clazz, c);
                if (mergedAnnotation != null)
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
            Manager manager = ClassUtil.getMergedAnnotation(clazz, Manager.class);
            if (manager != null)
            {
                if (ClassUtil.getAnnotation(clazz, AutoPlugin.class) != null || (ClassUtil.getAnnotation(clazz, AutoPlugin.class) != null) || (ReflectionUtils.containAnnotaitonsInFieldOrMethod(clazz, BitConstants.or, AutoPlugin.class, Autowired.class).block().getRet()))
                {
                    throw new ProgramaException("manager is not suggested to act like bean");
                }

                IReflectManager instance = (IReflectManager) ReflectUtil.newInstance(clazz);
                String name = manager.name();
                synchronized (this.managers)
                {
                    instance = instance.createOrDefault();
                    AnnotaionManagerWrapper wrapper = this.managers.get(name);
                    if (wrapper != null)
                    {
                        LOG.warn(Module.CONTAINER, "重复的managerName,{}", name);
                        if (manager.override())
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
            if (managerNode == null)
            {
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
                    LOG.info(Module.CONTAINER, "add node ,group:{},node:{}", group, name);
                }
            } catch (Exception e)
            {
                LOG.warning(Module.CONTAINER, e, "实例化失败:{}", e.getMessage());
            }
        }
    }

//    private void firstAfterScan(MultiFilter filter) throws Exception
//    {
////        List<Class<? extends IManagerNodeFactory>> factories = new ArrayList<>(filter.nodeFactories);
////        Collections.sort(factories, new OrderComparator());
//        Set<String> keys = filter.managers.keySet();
//
//        Map<String, List<AnnotationNodeWrapper>> annotationNodes = new HashMap<>(filter.annotationNodes);
//        for (String key : keys)
//        {
//            if (annotationNodes.containsKey(key)) continue;
//            annotationNodes.put(key, new ArrayList<>());
//        }
//
//
//        for (String key : annotationNodes.keySet())
//        {
//            List<AnnotationNodeWrapper> objects = annotationNodes.get(key);
//            if (CollectionUtils.isEmpty(objects)) continue;
//            Collections.sort(objects, (o1, o2) -> ClassUtil.ordererCompare(o1.getClass(), o2.getClass()));
//            AnnotaionManagerWrapper annotaionManagerWrapper = filter.managers.get(key);
//            if (annotaionManagerWrapper == null)
//            {
//                continue;
//            }
//            String name = null;
//            String group = null;
//            boolean override = false;
//            for (AnnotationNodeWrapper wp : objects)
//            {
//                Object object = wp.getNode();
//                name = object.getClass().getAnnotation(ManagerNode.class).name();
//                group = object.getClass().getAnnotation(ManagerNode.class).group();
//                override = object.getClass().getAnnotation(ManagerNode.class).override();
//                boolean contains = annotaionManagerWrapper.getManagerNodes().containsKey(name);
//                if (contains)
//                {
//                    LOG.warn(Module.CONTAINER, "重复的node,group:{},name:{}", group, name);
//                }
//                if (!contains || override)
//                {
//                    annotaionManagerWrapper.getManagerNodes().put(name, wp);
//                }
//            }
//        }
//    }
}
