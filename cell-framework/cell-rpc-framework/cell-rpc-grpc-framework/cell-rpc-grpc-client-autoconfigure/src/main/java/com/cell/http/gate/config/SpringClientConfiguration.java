package com.cell.http.gate.config;

import com.cell.channelfactory.GRPCChannelFactory;
import com.cell.channelfactory.GrpcChannelConfigurer;
import com.cell.channelfactory.impl.InProcessChannelFactory;
import com.cell.channelfactory.impl.InProcessOrAlternativeChannelFactory;
import com.cell.channelfactory.impl.NettyChannelFactory;
import com.cell.interceptor.GlobalClientInterceptorRegistry;
import com.cell.postprocessor.GRPCClientPostProcessor;
import com.cell.stub.impl.AsyncFutureStubFactory;
import com.cell.stub.impl.BlockingStubFactory;
import com.cell.stub.impl.FutureStubFactory;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import org.springframework.beans.factory.InitializingBean;
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
public class SpringClientConfiguration implements InitializingBean
{
    @Bean
    static GRPCClientPostProcessor grpcClientBeanPostProcessor(final ApplicationContext applicationContext)
    {
        return new GRPCClientPostProcessor(applicationContext);
    }

    @Bean
    AsyncFutureStubFactory asyncStubFactory()
    {
        return new AsyncFutureStubFactory();
    }

    @Bean
    BlockingStubFactory blockingStubFactory()
    {
        return new BlockingStubFactory();
    }

    @Bean
    FutureStubFactory futureStubFactory()
    {
        return new FutureStubFactory();
    }

//    @Bean
//    GRPCConfiguration grpcChannelsProperties()
//    {
//        return GRPCConfiguration.getInstance();
//    }

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
    public GRPCChannelFactory nettyChannelFactory(
                                                  final GlobalClientInterceptorRegistry globalClientInterceptorRegistry,
                                                  final List<GrpcChannelConfigurer> channelConfigurers)
    {
        final NettyChannelFactory channelFactory =
                new NettyChannelFactory(globalClientInterceptorRegistry, channelConfigurers);
        final InProcessChannelFactory inProcessChannelFactory =
                new InProcessChannelFactory( globalClientInterceptorRegistry, channelConfigurers);
        return new InProcessOrAlternativeChannelFactory(inProcessChannelFactory, channelFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {

    }
}
