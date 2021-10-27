package com.cell.stub.impl;

import com.cell.stub.IStubFactory;
import io.grpc.Channel;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.BeanInstantiationException;

import java.lang.reflect.Method;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 09:49
 */
public abstract class AbstractStandardJavaStubFactory implements IStubFactory
{
    @Override
    public AbstractStub<?> createStub(final Class<? extends AbstractStub<?>> stubType, final Channel channel)
    {
        try
        {
            final String methodName = getFactoryMethodName();
            final Class<?> enclosingClass = stubType.getEnclosingClass();
            final Method factoryMethod = enclosingClass.getMethod(methodName, Channel.class);
            return stubType.cast(factoryMethod.invoke(null, channel));
        } catch (final Exception e)
        {
            throw new BeanInstantiationException(stubType, "Failed to create gRPC client", e);
        }
    }


    protected abstract String getFactoryMethodName();
}
