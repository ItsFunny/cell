package com.cell.postprocessor;

import com.cell.adapter.IBeanPostProcessortAdapter;
import com.cell.rpc.client.base.framework.annotation.GRPCClient;
import com.cell.rpc.client.base.framework.annotation.GrpcClientBean;
import com.cell.channelfactory.GRPCChannelFactory;
import com.cell.inject.StubTransformer;
import com.cell.stub.IStubFactory;
import com.google.common.collect.Lists;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 10:45
 */
public class GRPCClientPostProcessor implements IBeanPostProcessortAdapter
{
    private final ApplicationContext applicationContext;
    private ConfigurableListableBeanFactory configurableBeanFactory;

    public GRPCClientPostProcessor(ApplicationContext applicationContext) {this.applicationContext = applicationContext;}

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException
    {
        Class<?> clazz = bean.getClass();
        do
        {
            processFields(clazz, bean);
            processMethods(clazz, bean);

            if (isAnnotatedWithConfiguration(clazz))
            {
                processGrpcClientBeansAnnotations(clazz);
            }

            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
        return bean;
    }

    private void processGrpcClientBeansAnnotations(final Class<?> clazz)
    {
        for (final GrpcClientBean annotation : clazz.getAnnotationsByType(GrpcClientBean.class))
        {
            final String beanNameToCreate = getBeanName(annotation);
            try
            {
                final ConfigurableListableBeanFactory beanFactory = getConfigurableBeanFactory();
                final Object beanValue =
                        processInjectionPoint(null, annotation.clazz(), annotation.client());
                beanFactory.registerSingleton(beanNameToCreate, beanValue);
            } catch (final Exception e)
            {
                throw new BeanCreationException(annotation + " on class " + clazz.getName(), beanNameToCreate,
                        "Unexpected exception while creating and registering bean",
                        e);
            }
        }
    }

    private ConfigurableListableBeanFactory getConfigurableBeanFactory()
    {
        if (this.configurableBeanFactory == null)
        {
            this.configurableBeanFactory = ((ConfigurableApplicationContext) this.applicationContext).getBeanFactory();
        }
        return this.configurableBeanFactory;
    }

    private String getBeanName(final GrpcClientBean grpcClientBean)
    {
        if (!grpcClientBean.beanName().isEmpty())
        {
            return grpcClientBean.beanName();
        } else
        {
            return grpcClientBean.client().value() + grpcClientBean.clazz().getSimpleName();
        }
    }

    private boolean isAnnotatedWithConfiguration(final Class<?> clazz)
    {
        final Configuration configurationAnnotation = AnnotationUtils.findAnnotation(clazz, Configuration.class);
        return configurationAnnotation != null;
    }

    private void processFields(final Class<?> clazz, final Object bean)
    {
        for (final Field field : clazz.getDeclaredFields())
        {
            final GRPCClient annotation = AnnotationUtils.findAnnotation(field, GRPCClient.class);
            if (annotation != null)
            {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, processInjectionPoint(field, field.getType(), annotation));
            }
        }
    }

    protected <T> T processInjectionPoint(final Member injectionTarget, final Class<T> injectionType,
                                          final GRPCClient annotation)
    {
        final List<ClientInterceptor> interceptors = interceptorsFromAnnotation(annotation);
        final String name = annotation.value();
        final Channel channel;
        try
        {
            channel = getChannelFactory().createChannel(name, interceptors, annotation.sortInterceptors());
            if (channel == null)
            {
                throw new IllegalStateException("Channel factory created a null channel for " + name);
            }
        } catch (final RuntimeException e)
        {
            throw new IllegalStateException("Failed to create channel: " + name, e);
        }

        final T value = valueForMember(name, injectionTarget, injectionType, channel);
        if (value == null)
        {
            throw new IllegalStateException(
                    "Injection value is null unexpectedly for " + name + " at " + injectionTarget);
        }

        return value;
    }

    private GRPCChannelFactory channelFactory = null;

    protected <T> T valueForMember(final String name, final Member injectionTarget,
                                   final Class<T> injectionType,
                                   final Channel channel) throws BeansException
    {
        if (Channel.class.equals(injectionType))
        {
            return injectionType.cast(channel);
        } else if (AbstractStub.class.isAssignableFrom(injectionType))
        {

            @SuppressWarnings("unchecked") // Eclipse incorrectly marks this as not required
                    AbstractStub<?> stub = createStub(
                    (Class<? extends AbstractStub<?>>) injectionType.asSubclass(AbstractStub.class), channel);
            for (final StubTransformer stubTransformer : getStubTransformers())
            {
                stub = stubTransformer.transform(name, stub);
            }
            return injectionType.cast(stub);
        } else
        {
            if (injectionTarget != null)
            {
                throw new InvalidPropertyException(injectionTarget.getDeclaringClass(), injectionTarget.getName(),
                        "Unsupported type " + injectionType.getName());
            } else
            {
                throw new BeanInstantiationException(injectionType, "Unsupported grpc stub or channel type");
            }
        }
    }

    private List<StubTransformer> stubTransformers = null;

    private List<StubTransformer> getStubTransformers()
    {
        if (this.stubTransformers == null)
        {
            final Collection<StubTransformer> transformers =
                    this.applicationContext.getBeansOfType(StubTransformer.class).values();
            this.stubTransformers = new ArrayList<>(transformers);
            return this.stubTransformers;
        }
        return this.stubTransformers;
    }

    private AbstractStub<?> createStub(final Class<? extends AbstractStub<?>> stubClass, final Channel channel)
    {
        final IStubFactory factory = getStubFactories().stream()
                .filter(stubFactory -> stubFactory.isApplicable(stubClass))
                .findFirst()
                .orElseThrow(() -> new BeanInstantiationException(stubClass,
                        "Unsupported stub type: " + stubClass.getName() + " -> Please report this issue."));

        try
        {
            return factory.createStub(stubClass, channel);
        } catch (final Exception exception)
        {
            throw new BeanInstantiationException(stubClass, "Failed to create gRPC stub of type " + stubClass.getName(),
                    exception);
        }
    }

    private List<IStubFactory> stubFactories = null;

    private List<IStubFactory> getStubFactories()
    {
        if (this.stubFactories == null)
        {
            this.stubFactories = new ArrayList<>(this.applicationContext.getBeansOfType(IStubFactory.class).values());
        }
        return this.stubFactories;
    }

    private GRPCChannelFactory getChannelFactory()
    {
        if (this.channelFactory == null)
        {
            final GRPCChannelFactory factory = this.applicationContext.getBean(GRPCChannelFactory.class);
            this.channelFactory = factory;
            return factory;
        }
        return this.channelFactory;
    }

    protected List<ClientInterceptor> interceptorsFromAnnotation(final GRPCClient annotation) throws BeansException
    {
        final List<ClientInterceptor> list = Lists.newArrayList();
        for (final Class<? extends ClientInterceptor> interceptorClass : annotation.interceptors())
        {
            final ClientInterceptor clientInterceptor;

            if (this.applicationContext.getBeanNamesForType(interceptorClass).length > 0)
            {
                clientInterceptor = this.applicationContext.getBean(interceptorClass);
            } else
            {
                try
                {
                    clientInterceptor = interceptorClass.getConstructor().newInstance();
                } catch (final Exception e)
                {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            list.add(clientInterceptor);
        }
        for (final String interceptorName : annotation.interceptorNames())
        {
            list.add(this.applicationContext.getBean(interceptorName, ClientInterceptor.class));
        }
        return list;
    }

    /**
     * Processes the bean's methods in the given class.
     *
     * @param clazz The class to process.
     * @param bean  The bean to process.
     */
    private void processMethods(final Class<?> clazz, final Object bean)
    {
        for (final Method method : clazz.getDeclaredMethods())
        {
            final GRPCClient annotation = AnnotationUtils.findAnnotation(method, GRPCClient.class);
            if (annotation != null)
            {
                final Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1)
                {
                    throw new BeanDefinitionStoreException(
                            "Method " + method + " doesn't have exactly one parameter.");
                }
                ReflectionUtils.makeAccessible(method);
                ReflectionUtils.invokeMethod(method, bean,
                        processInjectionPoint(method, paramTypes[0], annotation));
            }
        }
    }
}
