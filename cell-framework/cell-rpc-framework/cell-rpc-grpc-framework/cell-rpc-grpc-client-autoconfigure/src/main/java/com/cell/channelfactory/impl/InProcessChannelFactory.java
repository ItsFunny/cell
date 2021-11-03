package com.cell.channelfactory.impl;

import com.cell.channelfactory.GrpcChannelConfigurer;
import com.cell.config.GRPCConfiguration;
import com.cell.interceptor.GlobalClientInterceptorRegistry;
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
    public InProcessChannelFactory(final GRPCConfiguration properties,
                                   final GlobalClientInterceptorRegistry globalClientInterceptorRegistry)
    {
        this(properties, globalClientInterceptorRegistry, Collections.emptyList());
    }

    public InProcessChannelFactory(final GRPCConfiguration properties,
                                   final GlobalClientInterceptorRegistry globalClientInterceptorRegistry,
                                   final List<GrpcChannelConfigurer> channelConfigurers)
    {
        super(properties, globalClientInterceptorRegistry, channelConfigurers);
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