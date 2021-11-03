package com.cell.channelfactory.impl;

import com.cell.channelfactory.GrpcChannelConfigurer;
import com.cell.discovery.nacos.config.GRPCConfiguration;
import com.cell.discovery.nacos.config.GrpcChannelProperties;
import com.cell.interceptor.GlobalClientInterceptorRegistry;
import com.cell.nameresolver.StaticNameResolverProvider;
import com.cell.utils.GrpcUtils;
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

    public NettyChannelFactory(GRPCConfiguration properties, GlobalClientInterceptorRegistry globalClientInterceptorRegistry, List<GrpcChannelConfigurer> channelConfigurers)
    {
        super(properties, globalClientInterceptorRegistry, channelConfigurers);
    }

    @Override
    protected NettyChannelBuilder newChannelBuilder(String name)
    {
        final GrpcChannelProperties properties = getPropertiesFor(name);
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
