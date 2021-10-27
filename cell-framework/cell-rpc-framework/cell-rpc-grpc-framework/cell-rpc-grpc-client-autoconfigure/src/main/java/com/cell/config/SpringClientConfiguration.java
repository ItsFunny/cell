package com.cell.config;

import com.cell.channelfactory.GRPCChannelFactory;
import com.cell.channelfactory.GrpcChannelConfigurer;
import com.cell.channelfactory.impl.InProcessChannelFactory;
import com.cell.channelfactory.impl.InProcessOrAlternativeChannelFactory;
import com.cell.channelfactory.impl.NettyChannelFactory;
import com.cell.interceptor.GlobalClientInterceptorRegistry;
import com.cell.postprocessor.GRPCClientPostProcessor;
import com.cell.stub.impl.AsyncFutureStubFactory;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 09:56
 */
@Configuration
public class SpringClientConfiguration
{
    @Bean
    static GRPCClientPostProcessor grpcClientBeanPostProcessor(final ApplicationContext applicationContext) {
        return new GRPCClientPostProcessor(applicationContext);
    }
    @Bean
    AsyncFutureStubFactory asyncStubFactory()
    {
        return new AsyncFutureStubFactory();
    }

    @Bean
    GRPCConfiguration grpcChannelsProperties()
    {
        return GRPCConfiguration.getInstance();
    }

    @Bean
    GlobalClientInterceptorRegistry globalClientInterceptorRegistry(final ApplicationContext applicationContext)
    {
        return new GlobalClientInterceptorRegistry(applicationContext);
    }

    @Bean
    GrpcChannelConfigurer decompressionChannelConfigurer(final DecompressorRegistry registry)
    {
        return (builder, name) -> builder.decompressorRegistry(registry);
    }

    @Bean
    GrpcChannelConfigurer compressionChannelConfigurer(final CompressorRegistry registry)
    {
        return (builder, name) -> builder.compressorRegistry(registry);
    }

    @Bean
    public GRPCChannelFactory nettyChannelFactory(final GRPCConfiguration properties,
                                                  final GlobalClientInterceptorRegistry globalClientInterceptorRegistry,
                                                  final List<GrpcChannelConfigurer> channelConfigurers)
    {
        final NettyChannelFactory channelFactory =
                new NettyChannelFactory(properties, globalClientInterceptorRegistry, channelConfigurers);
        final InProcessChannelFactory inProcessChannelFactory =
                new InProcessChannelFactory(properties, globalClientInterceptorRegistry, channelConfigurers);
        return new InProcessOrAlternativeChannelFactory(properties, inProcessChannelFactory, channelFactory);
    }
}
