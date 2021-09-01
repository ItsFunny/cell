package com.cell.postprocessors.dependency;

import com.cell.adapter.IBeanDefinitionRegistryPostProcessorAdapter;
import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.log.LOG;
import com.cell.models.Module;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpringBeanDependenciesPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
        implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware, IBeanPostProcessortAdapter
{

    public SpringBeanDependenciesPostProcessor()
    {

    }


    private boolean requiredParameterValue = true;
    private String requiredParameterName = "required";
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();
    @Nullable
    private ConfigurableListableBeanFactory beanFactory;
    private int order = Ordered.LOWEST_PRECEDENCE - 1;
    private List<IDependenciesFilter> filters = new ArrayList<>();
    private BeanDependenciesMap dependenciesMap = new BeanDependenciesMap();

    public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes)
    {
        Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
    }


    public BeanDependenciesMap getDependenciesMap()
    {
        return dependenciesMap;
    }

    public void addDependOn(Object target, Object dependOn)
    {
        if (dependenciesMap.addDependenOn(target.getClass(), dependOn.getClass()))
        {
            LOG.debug(Module.CONTAINER, "object {} add dependOn {}", target.getClass(), dependOn.getClass());
        }
    }

    public void addDependOnList(Object target, Object[] dependOns)
    {
        for (Object dependOn : dependOns)
        {
            if (dependOn != null)
            {
                addDependOn(target, dependOn);
            }
        }
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    @Override
    public int getOrder()
    {
        return this.order;
    }

    boolean targetable(Object bean, String beanName)
    {
        for (IDependenciesFilter filter : filters)
        {
            if (!filter.targetable(bean.getClass(), beanName))
            {
                return false;
            }
        }
        return true;
    }

    boolean dependable(Object bean, String beanName, Object dependOn)
    {
        for (IDependenciesFilter filter : filters)
        {
            if (!filter.dependable(bean.getClass(), beanName, dependOn))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory)
    {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory))
        {
            throw new IllegalArgumentException(
                    "BeanDependenciesPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName)
    {
        if (targetable(beanType, beanName))
        {
            InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
            //metadata.checkConfigMembers(beanDefinition);
        }
    }


    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException
    {
        if (targetable(bean.getClass(), beanName))
        {
            InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
            try
            {
                metadata.inject(bean, beanName, pvs);
            } catch (BeanCreationException ex)
            {
                throw ex;
            } catch (Throwable ex)
            {
                throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
            }
        }
        return pvs;
    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, @Nullable PropertyValues pvs)
    {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        // Quick check on the concurrent map first, with minimal locking.
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz))
        {
            synchronized (this.injectionMetadataCache)
            {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz))
                {
                    if (metadata != null)
                    {
                        metadata.clear(pvs);
                    }
                    metadata = buildAutowiringMetadata(clazz);
                    this.injectionMetadataCache.put(cacheKey, metadata);
                }
            }
        }
        return metadata;
    }

    private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz)
    {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        Class<?> targetClass = clazz;

        do
        {
            final List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();

            ReflectionUtils.doWithLocalFields(targetClass, field ->
            {
                AnnotationAttributes ann = findAutowiredAnnotation(field);
                if (ann != null)
                {
                    if (Modifier.isStatic(field.getModifiers()))
                    {
                        LOG.warn(Module.CONTAINER, "Autowired or AutoPlugin annotation is not supported on static fields: " + field);
                        return;
                    }
                    boolean required = determineRequiredStatus(ann);
                    currElements.add(new FieldDependOnElement(field, required));
                }
            });

            ReflectionUtils.doWithLocalMethods(targetClass, method ->
            {
                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod))
                {
                    return;
                }
                AnnotationAttributes ann = findAutowiredAnnotation(bridgedMethod);
                if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz)))
                {
                    if (Modifier.isStatic(method.getModifiers()))
                    {
                        LOG.warn(Module.CONTAINER, "Autowired or AutoPlugin annotation is not supported on static methods: " + method);
                        return;
                    }
                    if (method.getParameterCount() == 0)
                    {
                        LOG.warn(Module.CONTAINER, "Autowired or AutoPlugin annotation should only be used on methods with parameters: " +
                                method);
                    }
                    boolean required = determineRequiredStatus(ann);
                    PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                    currElements.add(new MethodDependOnElement(method, required, pd));
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz, elements);
    }

    protected boolean determineRequiredStatus(AnnotationAttributes ann)
    {
        return (!ann.containsKey(this.requiredParameterName) ||
                this.requiredParameterValue == ann.getBoolean(this.requiredParameterName));
    }

    @Nullable
    private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao)
    {
        if (ao.getAnnotations().length > 0)
        {
            for (Class<? extends Annotation> type : this.autowiredAnnotationTypes)
            {
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null)
                {
                    return attributes;
                }
            }
        }
        return null;
    }


    /**
     * Resolve the specified cached method argument or field value.
     */
    @Nullable
    private Object resolvedCachedArgument(@Nullable String beanName, @Nullable Object cachedArgument)
    {
        if (cachedArgument instanceof DependencyDescriptor)
        {
            DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
            Assert.state(this.beanFactory != null, "No BeanFactory available");
            return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
        } else
        {
            return cachedArgument;
        }
    }

    /**
     * Class representing injection information about an annotated field.
     */
    private class FieldDependOnElement extends InjectionMetadata.InjectedElement
    {

        private final boolean required;

        private volatile boolean cached = false;

        @Nullable
        private volatile Object cachedFieldValue;

        public FieldDependOnElement(Field field, boolean required)
        {
            super(field, null);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable
        {
            Field field = (Field) this.member;
            Object value = null;
            if (this.cached)
            {
                value = resolvedCachedArgument(beanName, this.cachedFieldValue);
            } else
            {
                DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                desc.setContainingClass(bean.getClass());
                Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
                Assert.state(beanFactory != null, "No BeanFactory available");
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                try
                {
                    value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                } catch (BeansException ex)
                {
                    throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
                }
                synchronized (this)
                {
                    if (!this.cached)
                    {
                        if (value != null || this.required)
                        {
                            this.cachedFieldValue = desc;
                            //registerDependentBeans(beanName, autowiredBeanNames);
                            if (autowiredBeanNames.size() == 1)
                            {
                                String autowiredBeanName = autowiredBeanNames.iterator().next();
                                if (beanFactory.containsBean(autowiredBeanName) &&
                                        beanFactory.isTypeMatch(autowiredBeanName, field.getType()))
                                {
                                    this.cachedFieldValue = new ShortcutDependencyDescriptor(
                                            desc, autowiredBeanName, field.getType());
                                }
                            }
                        } else
                        {
                            this.cachedFieldValue = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (value != null)
            {
                addDependOn(bean, value);
            }
        }

    }


    /**
     * Class representing injection information about an annotated method.
     */
    private class MethodDependOnElement extends InjectionMetadata.InjectedElement
    {

        private final boolean required;

        private volatile boolean cached = false;

        @Nullable
        private volatile Object[] cachedMethodArguments;

        public MethodDependOnElement(Method method, boolean required, @Nullable PropertyDescriptor pd)
        {
            super(method, pd);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable
        {
//			if (checkPropertySkipping(pvs)) {
//				return;
//			}
            Method method = (Method) this.member;
            Object[] arguments = null;
            if (this.cached)
            {
                // Shortcut for avoiding synchronization...
                arguments = resolveCachedArguments(beanName);
            } else
            {
                Class<?>[] paramTypes = method.getParameterTypes();
                arguments = new Object[paramTypes.length];
                DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
                Set<String> autowiredBeans = new LinkedHashSet<>(paramTypes.length);
                Assert.state(beanFactory != null, "No BeanFactory available");
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                for (int i = 0; i < arguments.length; i++)
                {
                    MethodParameter methodParam = new MethodParameter(method, i);
                    DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
                    currDesc.setContainingClass(bean.getClass());
                    descriptors[i] = currDesc;
                    try
                    {
                        Object arg = beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
                        if (arg == null && !this.required)
                        {
                            arguments = null;
                            break;
                        }
                        arguments[i] = arg;
                    } catch (BeansException ex)
                    {
                        throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
                    }
                }
                synchronized (this)
                {
                    if (!this.cached)
                    {
                        if (arguments != null)
                        {
                            Object[] cachedMethodArguments = new Object[paramTypes.length];
                            System.arraycopy(descriptors, 0, cachedMethodArguments, 0, arguments.length);
                            //registerDependentBeans(beanName, autowiredBeans);
                            if (autowiredBeans.size() == paramTypes.length)
                            {
                                Iterator<String> it = autowiredBeans.iterator();
                                for (int i = 0; i < paramTypes.length; i++)
                                {
                                    String autowiredBeanName = it.next();
                                    if (beanFactory.containsBean(autowiredBeanName) &&
                                            beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i]))
                                    {
                                        cachedMethodArguments[i] = new ShortcutDependencyDescriptor(
                                                descriptors[i], autowiredBeanName, paramTypes[i]);
                                    }
                                }
                            }
                            this.cachedMethodArguments = cachedMethodArguments;
                        } else
                        {
                            this.cachedMethodArguments = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (arguments != null)
            {
                addDependOnList(bean, arguments);
            }
        }

        @Nullable
        private Object[] resolveCachedArguments(@Nullable String beanName)
        {
            Object[] cachedMethodArguments = this.cachedMethodArguments;
            if (cachedMethodArguments == null)
            {
                return null;
            }
            Object[] arguments = new Object[cachedMethodArguments.length];
            for (int i = 0; i < arguments.length; i++)
            {
                arguments[i] = resolvedCachedArgument(beanName, cachedMethodArguments[i]);
            }
            return arguments;
        }
    }

    @SuppressWarnings("serial")
    private static class ShortcutDependencyDescriptor extends DependencyDescriptor
    {

        private final String shortcut;

        private final Class<?> requiredType;

        public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType)
        {
            super(original);
            this.shortcut = shortcut;
            this.requiredType = requiredType;
        }

        @Override
        public Object resolveShortcut(BeanFactory beanFactory)
        {
            return beanFactory.getBean(this.shortcut, this.requiredType);
        }
    }


}
