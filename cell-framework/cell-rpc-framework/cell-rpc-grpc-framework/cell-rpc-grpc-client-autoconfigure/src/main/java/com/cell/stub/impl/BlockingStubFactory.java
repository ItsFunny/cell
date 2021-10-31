package com.cell.stub.impl;

import io.grpc.stub.AbstractBlockingStub;
import io.grpc.stub.AbstractStub;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 19:31
 */
public class BlockingStubFactory extends AbstractStandardJavaStubFactory
{

    @Override
    protected String getFactoryMethodName()
    {
        return "newBlockingStub";
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractStub<?>> stubType)
    {
        return AbstractBlockingStub.class.isAssignableFrom(stubType);
    }
}
