package com.cell.channelfactory;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ConnectivityState;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 18:50
 */
public interface GRPCChannelFactory extends AutoCloseable
{
    default Channel createChannel(final String name)
    {
        return createChannel(name, Collections.emptyList());
    }

    default Channel createChannel(final String name, final List<ClientInterceptor> interceptors)
    {
        return createChannel(name, interceptors, false);
    }

    Channel createChannel(String name, List<ClientInterceptor> interceptors, boolean sortInterceptors);

    default Map<String, ConnectivityState> getConnectivityState()
    {
        return Collections.emptyMap();
    }

    @Override
    void close();
}
