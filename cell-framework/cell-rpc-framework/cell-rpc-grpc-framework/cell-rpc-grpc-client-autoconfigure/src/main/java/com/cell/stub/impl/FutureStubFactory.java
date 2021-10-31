package com.cell.stub.impl;

import io.grpc.stub.AbstractFutureStub;
import io.grpc.stub.AbstractStub;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 19:32
 */
public class FutureStubFactory extends AbstractStandardJavaStubFactory
{

    @Override
    public boolean isApplicable(Class<? extends AbstractStub<?>> stubType)
    {
        return AbstractFutureStub.class.isAssignableFrom(stubType);
    }

    @Override
    protected String getFactoryMethodName()
    {
        return "newFutureStub";
    }
}
