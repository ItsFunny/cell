package com.cell.initializer;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.Exclude;
import com.cell.annotations.ManagerNode;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.extension.AbstractNodeExtension;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.manager.IManagerFactory;
import com.cell.manager.IManagerNodeFactory;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.postprocessor.SpringBeanRegistry;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import io.netty.util.internal.ConcurrentSet;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import sun.reflect.misc.ReflectUtil;

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
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {
        this.initOnce(null);
        applicationContext.addBeanFactoryPostProcessor(SpringBeanRegistry.getInstance());
    }


    @Override
    protected void onInit(InitCTX ctx)
    {
        SpringBeanRegistry.getInstance().initOnce(ctx);
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

        //        final List<Class<? extends Annotation>> interestAnnotations = Arrays.asList(HttpCmdAnno.class);
        final List<Class<? extends Annotation>> interestAnnotations = Arrays.asList();
        final Map<Class<? extends Annotation>, List<Class<?>>> interestAnnotationsClazzs = new ConcurrentHashMap<>();

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
                        this.annotationNodes.put(group, objects);
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


}
