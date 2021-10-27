package com.cell.config;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 05:01
 */

import com.cell.interceptor.GlobalServerInterceptorRegistry;
import com.cell.serverfactory.GrpcServerConfigurer;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ProtoReflectionService.class)
//@ConditionalOnProperty(prefix = "grpc.server", name = "reflection-service-enabled", matchIfMissing = true)
public class GRPCSpringConfiguration
{
//    @Bean
//    BindableService protoReflectionService()
//    {
//        return ProtoReflectionService.newInstance();
//    }

    @ConditionalOnMissingBean
    @Bean
    GlobalServerInterceptorRegistry globalServerInterceptorRegistry(
            final ApplicationContext applicationContext)
    {
        return new GlobalServerInterceptorRegistry(applicationContext);
    }


    @ConditionalOnBean(CompressorRegistry.class)
    @Bean
    public GrpcServerConfigurer compressionServerConfigurer(final CompressorRegistry registry)
    {
        return builder -> builder.compressorRegistry(registry);
    }


    @ConditionalOnBean(DecompressorRegistry.class)
    @Bean
    public GrpcServerConfigurer decompressionServerConfigurer(final DecompressorRegistry registry)
    {
        return builder -> builder.decompressorRegistry(registry);
    }

}
