package com.cell.channelfactory.impl;

import com.cell.channelfactory.GrpcChannelConfigurer;
import com.cell.grpc.client.autoconfigurer.config.GRPCClientConfiguration;
import com.cell.grpc.common.utils.GrpcUtils;
import com.cell.interceptor.GlobalClientInterceptorRegistry;
import com.cell.nameresolver.StaticNameResolverProvider;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollDomainSocketChannel;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.unix.DomainSocketAddress;

import java.net.URI;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 18:54
 */
public class NettyChannelFactory extends AbstractChannelFactory<NettyChannelBuilder>
{

    public NettyChannelFactory(GlobalClientInterceptorRegistry globalClientInterceptorRegistry, List<GrpcChannelConfigurer> channelConfigurers)
    {
        super(globalClientInterceptorRegistry, channelConfigurers);
    }

    @Override
    protected NettyChannelBuilder newChannelBuilder(String name)
    {
        GRPCClientConfiguration.GRPCClientConfigurationNode properties = getPropertiesFor(name);
        URI address = properties.getAddress();
        if (address == null)
        {
            address = URI.create(name);
        }
        // TODO
        if (GrpcUtils.DOMAIN_SOCKET_ADDRESS_SCHEME.equals(address.getScheme()))
        {
            final String path = GrpcUtils.extractDomainSocketAddressPath(address.toString());
            return NettyChannelBuilder.forAddress(new DomainSocketAddress(path))
                    .nameResolverFactory(new StaticNameResolverProvider())
                    .channelType(EpollDomainSocketChannel.class)
                    .usePlaintext()
                    .eventLoopGroup(new EpollEventLoopGroup());
        } else
        {
            return NettyChannelBuilder.forTarget(address.toString())
                    .nameResolverFactory(new StaticNameResolverProvider())
                    .usePlaintext()
                    .defaultLoadBalancingPolicy(properties.getDefaultLoadBalancingPolicy());
        }
    }

    @Override
    public void close()
    {

    }
}
