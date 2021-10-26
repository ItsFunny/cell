package com.cell.server;

import com.cell.config.GRPCServerConfiguration;
import com.cell.configuration.RootConfiguration;
import com.cell.constants.GRPCConstants;
import com.cell.context.InitCTX;
import com.cell.exceptions.ProgramaException;
import com.cell.extension.GRPCServerExtension;
import com.cell.proxy.IRPCServerProxy;
import com.cell.utils.GRPCUtils;
import com.google.common.net.InetAddresses;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:30
 */
public class DefaultGRPServer extends BaseRPCServer implements IGRPCServer
{

    public DefaultGRPServer(IRPCServerProxy proxy)
    {
        super(proxy);
    }


    @Override
    protected void onInit(InitCTX ctx)
    {
        super.onInit(ctx);

        Optional<Object> cfg = RootConfiguration.getInstance().getExtensionConfiguration(GRPCServerExtension.class);
        boolean present = cfg.isPresent();
        if (!present)
        {
            throw new ProgramaException("asd");
        }
        Object o = cfg.get();
        GRPCServerConfiguration configuration = (GRPCServerConfiguration) o;
//        DOMAIN_SOCKET_ADDRESS_PREFIX
        String address = configuration.getAddress();
        int port = configuration.getPort();
        NettyServerBuilder nettyServerBuilder = null;
        if (address.startsWith(GRPCConstants.DOMAIN_SOCKET_ADDRESS_PREFIX))
        {
            final String path = GRPCUtils.extractDomainSocketAddressPath(address);
            nettyServerBuilder = NettyServerBuilder.forAddress(new DomainSocketAddress(path))
                    .channelType(EpollServerDomainSocketChannel.class)
                    .bossEventLoopGroup(new io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup(1))
                    .workerEventLoopGroup(new EpollEventLoopGroup());
        } else if (GRPCConstants.ANY_IP_ADDRESS.equals(address))
        {
            nettyServerBuilder = NettyServerBuilder.forPort(port);
        } else
        {
            nettyServerBuilder = NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(address), port));
        }
        this.configure(nettyServerBuilder);
    }

    private void configure(NettyServerBuilder nettyServerBuilder)
    {

    }
    protected void configureServices(final ServerBuilder builder) {
        final Set<String> serviceNames = new LinkedHashSet<>();

//        for (final GrpcServiceDefinition service : this.serviceList) {
//            final String serviceName = service.getDefinition().getServiceDescriptor().getName();
//            if (!serviceNames.add(serviceName)) {
//                throw new IllegalStateException("Found duplicate service implementation: " + serviceName);
//            }
//            log.info("Registered gRPC service: " + serviceName + ", bean: " + service.getBeanName() + ", class: "
//                    + service.getBeanClazz().getName());
//            builder.addService(service.getDefinition());
//        }
    }
}
