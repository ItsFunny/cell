package com.cell.rpc.grpc.client.stub;

import io.grpc.Channel;
import io.grpc.stub.AbstractStub;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 09:48
 */
public interface IStubFactory
{
    AbstractStub<?> createStub(Class<? extends AbstractStub<?>> stubType, Channel channel);

    boolean isApplicable(Class<? extends AbstractStub<?>> stubType);
}
