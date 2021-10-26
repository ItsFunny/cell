package com.cell.config;

import com.cell.core.AnnotationGrpcCodecDiscoverer;
import com.cell.core.GrpcCodecDefinition;
import com.cell.core.GrpcCodecDiscoverer;
import com.cell.interceptor.GlobalServerInterceptorRegistry;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.serverfactory.GrpcServerConfigurer;
import io.grpc.BindableService;
import io.grpc.Codec;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-26 18:32
 */
@Configuration
@ConditionalOnClass(ProtoReflectionService.class)
//@ConditionalOnProperty(prefix = "grpc.server", name = "reflection-service-enabled", matchIfMissing = true)
public class GRPCSpringConfiguration
{
    @Bean
    BindableService protoReflectionService()
    {
        return ProtoReflectionService.newInstance();
    }

    @ConditionalOnMissingBean
    @Bean
    GlobalServerInterceptorRegistry globalServerInterceptorRegistry(
            final ApplicationContext applicationContext)
    {
        return new GlobalServerInterceptorRegistry(applicationContext);
    }

    @ConditionalOnMissingBean
    @Bean
    public GrpcCodecDiscoverer defaultGrpcCodecDiscoverer()
    {
        return new AnnotationGrpcCodecDiscoverer();
    }

    @ConditionalOnBean(GrpcCodecDiscoverer.class)
    @ConditionalOnMissingBean
    @Bean
    public CompressorRegistry defaultCompressorRegistry(final GrpcCodecDiscoverer codecDiscoverer)
    {
        LOG.info(Module.GRPC, "Found GrpcCodecDiscoverer -> Creating custom CompressorRegistry");
        final CompressorRegistry registry = CompressorRegistry.getDefaultInstance();
        for (final GrpcCodecDefinition definition : codecDiscoverer.findGrpcCodecs())
        {
            if (definition.getCodecType().isForCompression())
            {
                final Codec codec = definition.getCodec();
                LOG.info(Module.GRPC, "Registering compressor: '{}' ({})", codec.getMessageEncoding(), codec.getClass().getName());
                registry.register(codec);
            }
        }
        return registry;
    }

    @ConditionalOnBean(CompressorRegistry.class)
    @Bean
    public GrpcServerConfigurer compressionServerConfigurer(final CompressorRegistry registry)
    {
        return builder -> builder.compressorRegistry(registry);
    }

    @ConditionalOnBean(GrpcCodecDiscoverer.class)
    @ConditionalOnMissingBean
    @Bean
    public DecompressorRegistry defaultDecompressorRegistry(final GrpcCodecDiscoverer codecDiscoverer)
    {
        LOG.info(Module.GRPC, "Found GrpcCodecDiscoverer -> Creating custom DecompressorRegistry");
        DecompressorRegistry registry = DecompressorRegistry.getDefaultInstance();
        for (final GrpcCodecDefinition definition : codecDiscoverer.findGrpcCodecs())
        {
            if (definition.getCodecType().isForDecompression())
            {
                final Codec codec = definition.getCodec();
                final boolean isAdvertised = definition.isAdvertised();
                LOG.info(Module.GRPC, "Registering {} decompressor: '{}' ({})",
                        isAdvertised ? "advertised" : "", codec.getMessageEncoding(), codec.getClass().getName());
                registry = registry.with(codec, isAdvertised);
            }
        }
        return registry;
    }

    @ConditionalOnBean(DecompressorRegistry.class)
    @Bean
    public GrpcServerConfigurer decompressionServerConfigurer(final DecompressorRegistry registry)
    {
        return builder -> builder.decompressorRegistry(registry);
    }

}
