package com.cell.grpc.server.framework.server;

import com.cell.com.cell.grpc.common.config.GRPCServerConfiguration;
import com.cell.configuration.RootConfiguration;
import com.cell.context.InitCTX;
import com.cell.exceptions.ProgramaException;
import com.cell.grpc.server.framework.annotation.GRPCService;
import com.cell.grpc.server.framework.constants.GRPCConstants;
import com.cell.grpc.server.framework.interceptor.GlobalServerInterceptorRegistry;
import com.cell.grpc.server.framework.serverfactory.GrpcServerConfigurer;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.proxy.IRPCServerProxy;
import com.cell.root.Root;
import com.cell.server.AbstractBaseRPCServer;
import com.cell.utils.GrpcUtils;
import com.google.common.collect.Lists;
import com.google.common.net.InetAddresses;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import org.springframework.beans.factory.BeanCreationException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:30
 */
public class DefaultGRPServer extends AbstractBaseRPCServer implements IGRPCServer
{
    protected List<GrpcServerConfigurer> serverConfigurers;
    private Server server;

    public DefaultGRPServer(IRPCServerProxy proxy)
    {
        super(proxy);
        this.setPort((short) 12000);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        String[] beanNamesForType = Root.getApplicationContext().getBeanNamesForType(GrpcServerConfigurer.class);
        this.serverConfigurers = new ArrayList<>();
        for (String s : beanNamesForType)
        {
            this.serverConfigurers.add((GrpcServerConfigurer) Root.getBean(s));
        }
        Optional<GRPCServerConfiguration> cfg = RootConfiguration.getInstance().getConfigurationByType(GRPCServerConfiguration.class);
        boolean present = cfg.isPresent();
        if (!present)
        {
            throw new ProgramaException("asd");
        }
        GRPCServerConfiguration o = cfg.get();
        GRPCServerConfiguration configuration = o;
        String address = configuration.getAddress();
        int port = configuration.getPort();
        NettyServerBuilder nettyServerBuilder = null;
        if (address.startsWith(GRPCConstants.DOMAIN_SOCKET_ADDRESS_PREFIX))
        {
            final String path = GrpcUtils.extractDomainSocketAddressPath(address);
            nettyServerBuilder = NettyServerBuilder.forAddress(new DomainSocketAddress(path))
                    .channelType(EpollServerDomainSocketChannel.class)
                    .bossEventLoopGroup(new EpollEventLoopGroup(1))
                    .workerEventLoopGroup(new EpollEventLoopGroup());
        } else if (GRPCConstants.ANY_IP_ADDRESS.equals(address))
        {
            nettyServerBuilder = NettyServerBuilder.forPort(port);
        } else
        {
            nettyServerBuilder = NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(address), port));
        }
        this.configure(nettyServerBuilder);

        Server server = nettyServerBuilder.build();
        this.server = server;
        try
        {
            this.server.start();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        LOG.info(Module.GRPC, "grpc 启动, port:{}", configuration.getPort());
        final Thread awaitThread = new Thread(() ->
        {
            try
            {
                this.server.awaitTermination();
            } catch (final InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        });
        awaitThread.setName("grpc-server-container-");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    private void configure(NettyServerBuilder nettyServerBuilder)
    {
        Optional<GRPCServerConfiguration> configurationOb = RootConfiguration.getInstance().getConfigurationByType(GRPCServerConfiguration.class);
        GRPCServerConfiguration configuration = configurationOb.get();

        this.configureServices(nettyServerBuilder);
        this.configureKeepAlive(nettyServerBuilder);
        this.configureConnectionLimits(configuration, nettyServerBuilder);
        this.configureLimits(configuration, nettyServerBuilder);
        for (final GrpcServerConfigurer serverConfigurer : this.serverConfigurers)
        {
            serverConfigurer.accept(nettyServerBuilder);
        }
    }

    protected void configureLimits(GRPCServerConfiguration configuration, final NettyServerBuilder builder)
    {
        Integer maxInboundMessageSize = configuration.getMaxInboundMessageSize();
        Integer maxInboundMetadataSize = configuration.getMaxInboundMetadataSize();
        if (maxInboundMessageSize != null)
        {
            builder.maxInboundMessageSize(maxInboundMessageSize);
        }
        if (maxInboundMetadataSize != null)
        {
            builder.maxInboundMetadataSize(maxInboundMetadataSize);
        }
    }

    protected void configureConnectionLimits(GRPCServerConfiguration configuration, final NettyServerBuilder builder)
    {
        Long maxConnectionIdle = configuration.getMaxConnectionIdle();
        Long maxConnectionAge = configuration.getMaxConnectionAge();
        Long maxConnectionAgeGrace = configuration.getMaxConnectionAgeGrace();
        if (maxConnectionIdle != null)
        {
            builder.maxConnectionIdle(maxConnectionIdle, TimeUnit.NANOSECONDS);
        }
        if (maxConnectionAge != null)
        {
            builder.maxConnectionAge(maxConnectionAge, TimeUnit.NANOSECONDS);
        }
        if (maxConnectionAgeGrace != null)
        {
            builder.maxConnectionAgeGrace(maxConnectionAgeGrace, TimeUnit.NANOSECONDS);
        }
    }

    protected void configureKeepAlive(final NettyServerBuilder builder)
    {
        Optional<GRPCServerConfiguration> configurationOb = RootConfiguration.getInstance().getConfigurationByType(GRPCServerConfiguration.class);
        GRPCServerConfiguration configuration = configurationOb.get();
        long keepAliveTime = configuration.getKeepAliveTime();
        if (keepAliveTime > 0)
        {
            builder.keepAliveTime(keepAliveTime, TimeUnit.NANOSECONDS)
                    .keepAliveTimeout(configuration.getKeepAliveTimeOut(), TimeUnit.NANOSECONDS);
        }
        builder.permitKeepAliveTime(configuration.getPermitKeepAliveTime(), TimeUnit.NANOSECONDS)
                .permitKeepAliveWithoutCalls(configuration.isPermitKeepAliveWithoutCalls());
    }

    protected void configureServices(final NettyServerBuilder builder)
    {
        final Set<String> serviceNames = new LinkedHashSet<>();
        Collection<GrpcServiceDefinition> grpcServices = this.findGrpcServices();
        for (final GrpcServiceDefinition service : grpcServices)
        {
            final String serviceName = service.getDefinition().getServiceDescriptor().getName();
            if (!serviceNames.add(serviceName))
            {
                throw new IllegalStateException("Found duplicate service implementation: " + serviceName);
            }
            LOG.info(Module.GRPC, "Registered gRPC service: " + serviceName + ", bean: " + service.getBeanName() + ", class: "
                    + service.getBeanClazz().getName());
            builder.addService(service.getDefinition());
        }
    }

    public Collection<GrpcServiceDefinition> findGrpcServices()
    {
        Collection<String> beanNames = Root.getBeanByAnnotation(GRPCService.class);
        GlobalServerInterceptorRegistry globalServerInterceptorRegistry =
                Root.getApplicationContext().getBean(GlobalServerInterceptorRegistry.class);
        List<GrpcServiceDefinition> definitions = Lists.newArrayListWithCapacity(beanNames.size());
        for (String beanName : beanNames)
        {
            BindableService bindableService = Root.getBean(BindableService.class);
            ServerServiceDefinition serviceDefinition = bindableService.bindService();
            GRPCService grpcServiceAnnotation = Root.getApplicationContext().findAnnotationOnBean(beanName, GRPCService.class);
            serviceDefinition =
                    bindInterceptors(serviceDefinition, grpcServiceAnnotation, globalServerInterceptorRegistry);
            definitions.add(new GrpcServiceDefinition(beanName, bindableService.getClass(), serviceDefinition));
            LOG.info(Module.GRPC, "Found gRPC service: " + serviceDefinition.getServiceDescriptor().getName() + ", bean: "
                    + beanName + ", class: " + bindableService.getClass().getName());
        }
        return definitions;
    }

    private ServerServiceDefinition bindInterceptors(final ServerServiceDefinition serviceDefinition,
                                                     final GRPCService grpcServiceAnnotation,
                                                     final GlobalServerInterceptorRegistry globalServerInterceptorRegistry)
    {
        final List<ServerInterceptor> interceptors = Lists.newArrayList();
        interceptors.addAll(globalServerInterceptorRegistry.getServerInterceptors());
        for (final Class<? extends ServerInterceptor> interceptorClass : grpcServiceAnnotation.interceptors())
        {
            final ServerInterceptor serverInterceptor;
            if (Root.getApplicationContext().getBeanNamesForType(interceptorClass).length > 0)
            {
                serverInterceptor = Root.getApplicationContext().getBean(interceptorClass);
            } else
            {
                try
                {
                    serverInterceptor = interceptorClass.getConstructor().newInstance();
                } catch (final Exception e)
                {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            interceptors.add(serverInterceptor);
        }
        for (final String interceptorName : grpcServiceAnnotation.interceptorNames())
        {
            interceptors.add(Root.getApplicationContext().getBean(interceptorName, ServerInterceptor.class));
        }
        if (grpcServiceAnnotation.sortInterceptors())
        {
            globalServerInterceptorRegistry.sortInterceptors(interceptors);
        }
        return ServerInterceptors.interceptForward(serviceDefinition, interceptors);
    }

    protected void configureServices(final ServerBuilder builder)
    {
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

    @Override
    protected void onStart()
    {

    }
}
