package com.cell.stub.impl;

import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.AbstractStub;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 09:55
 */
public class AsyncFutureStubFactory extends AbstractStandardJavaStubFactory
{
    @Override
    public boolean isApplicable(Class<? extends AbstractStub<?>> stubType)
    {
        return AbstractAsyncStub.class.isAssignableFrom(stubType);
    }

    @Override
    protected String getFactoryMethodName()
    {
        return "newStub";
    }
}
