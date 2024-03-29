package com.cell.rpc.grpc.client.channelfactory.impl;

import com.cell.rpc.grpc.client.channelfactory.GrpcChannelConfigurer;
import com.cell.rpc.grpc.client.interceptor.GlobalClientInterceptorRegistry;
import io.grpc.inprocess.InProcessChannelBuilder;

import java.util.Collections;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 19:22
 */
public class InProcessChannelFactory extends AbstractChannelFactory<InProcessChannelBuilder>
{
    public InProcessChannelFactory(
            final GlobalClientInterceptorRegistry globalClientInterceptorRegistry)
    {
        this(globalClientInterceptorRegistry, Collections.emptyList());
    }

    public InProcessChannelFactory(
            final GlobalClientInterceptorRegistry globalClientInterceptorRegistry,
            final List<GrpcChannelConfigurer> channelConfigurers)
    {
        super(globalClientInterceptorRegistry, channelConfigurers);
    }

    @Override
    protected InProcessChannelBuilder newChannelBuilder(String name)
    {
        return InProcessChannelBuilder.forName(name);
    }

    @Override
    public void close()
    {

    }
}