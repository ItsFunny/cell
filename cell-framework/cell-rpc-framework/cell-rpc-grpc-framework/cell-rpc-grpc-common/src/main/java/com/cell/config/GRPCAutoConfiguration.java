package com.cell.config;

import com.cell.codec.AnnotationGrpcCodecDiscoverer;
import com.cell.codec.GrpcCodecDefinition;
import com.cell.codec.GrpcCodecDiscoverer;
import io.grpc.Codec;
import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 19:27
 */
@Configuration
public class GRPCAutoConfiguration
{

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
        final CompressorRegistry registry = CompressorRegistry.getDefaultInstance();
        for (final GrpcCodecDefinition definition : codecDiscoverer.findGrpcCodecs())
        {
            if (definition.getCodecType().isForCompression())
            {
                final Codec codec = definition.getCodec();
                registry.register(codec);
            }
        }
        return registry;
    }

    @ConditionalOnBean(GrpcCodecDiscoverer.class)
    @ConditionalOnMissingBean
    @Bean
    public DecompressorRegistry defaultDecompressorRegistry(final GrpcCodecDiscoverer codecDiscoverer)
    {
        DecompressorRegistry registry = DecompressorRegistry.getDefaultInstance();
        for (final GrpcCodecDefinition definition : codecDiscoverer.findGrpcCodecs())
        {
            if (definition.getCodecType().isForDecompression())
            {
                final Codec codec = definition.getCodec();
                final boolean isAdvertised = definition.isAdvertised();
                registry = registry.with(codec, isAdvertised);
            }
        }
        return registry;
    }
}
