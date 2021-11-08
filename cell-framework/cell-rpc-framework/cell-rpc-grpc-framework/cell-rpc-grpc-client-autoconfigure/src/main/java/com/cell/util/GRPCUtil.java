package com.cell.util;

import com.cell.channelfactory.GRPCChannelFactory;
import com.cell.com.cell.grpc.common.annotation.GRPCClient;
import com.cell.inject.StubTransformer;
import com.cell.stub.IStubFactory;
import com.google.common.collect.Lists;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import lombok.Data;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 11:01
 */
public class GRPCUtil
{
    @Data
    public static class GRPCClientWrapper<T>
    {
        private ApplicationContext applicationContext;
        private Member injectionTarget;
        private Class<T> injectionType;
        private Class<?> clazz;
        private Object bean;
        private GRPCClient annotation;

        public GRPCClientWrapper(ApplicationContext applicationContext, Member injectionTarget, Class<T> injectionType, Class<?> clazz, Object bean, GRPCClient annotation)
        {
            this.applicationContext = applicationContext;
            this.injectionTarget = injectionTarget;
            this.injectionType = injectionType;
            this.clazz = clazz;
            this.bean = bean;
            this.annotation = annotation;
        }
    }

    public static void processFields(final ApplicationContext applicationContext, final Class<?> clazz, final Object bean)
    {
        for (final Field field : clazz.getDeclaredFields())
        {
            final GRPCClient annotation = AnnotationUtils.findAnnotation(field, GRPCClient.class);
            if (annotation != null)
            {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, bean, processInjectionPoint(applicationContext, field, field.getType(), annotation));
            }
        }
    }

    private static <T> T processInjectionPoint(final ApplicationContext applicationContext, final Member injectionTarget, final Class<T> injectionType,
                                               final GRPCClient annotation)
    {
        final List<ClientInterceptor> interceptors = interceptorsFromAnnotation(applicationContext, annotation);
        final String name = annotation.value();
        final Channel channel;
        try
        {
            channel = getChannelFactory(applicationContext).createChannel(name, interceptors, annotation.sortInterceptors());
            if (channel == null)
            {
                throw new IllegalStateException("Channel factory created a null channel for " + name);
            }
        } catch (final RuntimeException e)
        {
            throw new IllegalStateException("Failed to create channel: " + name, e);
        }

        final T value = valueForMember(applicationContext, name, injectionTarget, injectionType, channel);
        if (value == null)
        {
            throw new IllegalStateException(
                    "Injection value is null unexpectedly for " + name + " at " + injectionTarget);
        }

        return value;
    }

    public static AbstractStub<?> createaaStub(final ApplicationContext applicationContext,
                                               Class<? extends AbstractStub<?>> clz,
                                               String name, List<ClientInterceptor> interceptors, boolean sort)
    {
        final Channel channel;
        try
        {
            channel = getChannelFactory(applicationContext).createChannel(name, interceptors, sort);
            if (channel == null)
            {
                throw new IllegalStateException("Channel factory created a null channel for " + name);
            }
        } catch (final RuntimeException e)
        {
            throw new IllegalStateException("Failed to create channel: " + name, e);
        }

        AbstractStub<?> stub = createStub(applicationContext, clz, channel);
        for (final StubTransformer stubTransformer : getStubTransformers(applicationContext))
        {
            stub = stubTransformer.transform(name, stub);
        }
        return stub;
    }

    public static List<ClientInterceptor> interceptorsFromAnnotation(final ApplicationContext applicationContext, final GRPCClient annotation) throws BeansException
    {
        final List<ClientInterceptor> list = Lists.newArrayList();
        for (final Class<? extends ClientInterceptor> interceptorClass : annotation.interceptors())
        {
            final ClientInterceptor clientInterceptor;

            if (applicationContext.getBeanNamesForType(interceptorClass).length > 0)
            {
                clientInterceptor = applicationContext.getBean(interceptorClass);
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
            list.add(applicationContext.getBean(interceptorName, ClientInterceptor.class));
        }
        return list;
    }

    private static GRPCChannelFactory getChannelFactory(ApplicationContext applicationContext)
    {
        final GRPCChannelFactory factory = applicationContext.getBean(GRPCChannelFactory.class);
        return factory;
    }

    private static <T> T valueForMember(ApplicationContext applicationContext,
                                        final String name, final Member injectionTarget,
                                        final Class<T> injectionType,
                                        final Channel channel) throws BeansException
    {
        if (Channel.class.equals(injectionType))
        {
            return injectionType.cast(channel);
        } else if (AbstractStub.class.isAssignableFrom(injectionType))
        {
            @SuppressWarnings("unchecked") // Eclipse incorrectly marks this as not required
                    AbstractStub<?> stub = createStub(applicationContext,
                    (Class<? extends AbstractStub<?>>) injectionType.asSubclass(AbstractStub.class), channel);
            for (final StubTransformer stubTransformer : getStubTransformers(applicationContext))
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

    private static List<StubTransformer> getStubTransformers(ApplicationContext applicationContext)
    {
        final Collection<StubTransformer> transformers =
                applicationContext.getBeansOfType(StubTransformer.class).values();
        return new ArrayList<>(transformers);
    }

    private static List<IStubFactory> getStubFactories(ApplicationContext applicationContext)
    {
        return new ArrayList<>(applicationContext.getBeansOfType(IStubFactory.class).values());
    }

    private static AbstractStub<?> createStub(ApplicationContext applicationContext, final Class<? extends AbstractStub<?>> stubClass, final Channel channel)
    {
        final IStubFactory factory = getStubFactories(applicationContext).stream()
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
}
