package com.cell.extension;

import com.cell.Configuration;
import com.cell.annotations.Plugin;
import com.cell.channel.DefaultRPCServerChannel;
import com.cell.config.GRPCServerConfiguration;
import com.cell.context.INodeContext;
import com.cell.dispatcher.IDispatcher;
import com.cell.dispatcher.impl.DefaultRPCServerCommandDispatcher;
import com.cell.manager.RPCHandlerManager;
import com.cell.proxy.DefaultRPCServerProxy;
import com.cell.proxy.IProxy;
import com.cell.proxy.IRPCServerProxy;
import com.cell.server.DefaultGRPServer;
import com.cell.server.IGRPCServer;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 10:30
 */
public class GRPCServerExtension extends AbstractSpringNodeExtension
{
    private static final String moduleName = "env.shared.rpc.grpc.json";

    private IGRPCServer server;
    private IDispatcher dispatcher;
    private IRPCServerProxy proxy;

//    private GrpcCodecDiscoverer codecDiscoverer;
//    private CompressorRegistry compressorRegistry;
//    private GrpcServerConfigurer compressionServerConfigurer;
//    private GrpcServerConfigurer decompressionServerConfigurer;
//    private DecompressorRegistry defaultDecompressorRegistry;
//    private GlobalServerInterceptorRegistry globalServerInterceptorRegistry;
//
//    @Plugin
//    public GlobalServerInterceptorRegistry globalServerInterceptorRegistry()
//    {
//        return this.globalServerInterceptorRegistry;
//    }
//
//    @Plugin
//    public DecompressorRegistry defaultDecompressor()
//    {
//        return this.defaultDecompressorRegistry;
//    }
//
//    @Plugin
//    public GrpcServerConfigurer decompressionServerConfigurer()
//    {
//        return this.decompressionServerConfigurer;
//    }
//
//    @Plugin
//    public GrpcServerConfigurer compreConfig()
//    {
//        return this.compressionServerConfigurer;
//    }
//
//    @Plugin
//    public CompressorRegistry registry()
//    {
//        return this.compressorRegistry;
//    }
//
//    @Plugin
//    public GrpcCodecDiscoverer codecDiscoverer()
//    {
//        return this.codecDiscoverer;
//    }

    @Bean(name = "grpcServer")
    public IGRPCServer server()
    {
        return this.server;
    }

    @Plugin(name = "grpcDispatcher")
    public IDispatcher dispatcher()
    {
        return dispatcher;
    }

    @Plugin(name = "grpcProxy")
    public IProxy proxy()
    {
        return this.proxy;
    }

    @Override
    public Object loadConfiguration(INodeContext ctx) throws Exception
    {
        try
        {
            GRPCServerConfiguration configuration = Configuration.getDefault().getConfigValue(moduleName).asObject(GRPCServerConfiguration.class);
            return configuration;
        } catch (Exception e)
        {
            return GRPCServerConfiguration.defaultConfiguration();
        }
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

        this.dispatcher = new DefaultRPCServerCommandDispatcher();
        this.proxy = new DefaultRPCServerProxy(this.dispatcher);
        this.server = new DefaultGRPServer(this.proxy);
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
//        this.codecDiscoverer = new AnnotationGrpcCodecDiscoverer();
//        this.compressorRegistry = CompressorRegistry.getDefaultInstance();
//        for (final GrpcCodecDefinition definition : codecDiscoverer.findGrpcCodecs())
//        {
//            if (definition.getCodecType().isForCompression())
//            {
//                final Codec codec = definition.getCodec();
//                LOG.info(Module.GRPC, "Registering compressor: '{}' ({})", codec.getMessageEncoding(), codec.getClass().getName());
//                this.compressorRegistry.register(codec);
//            }
//        }
//        this.compressionServerConfigurer = builder -> builder.compressorRegistry(this.compressorRegistry);
//        this.defaultDecompressorRegistry = DecompressorRegistry.getDefaultInstance();
//        for (final GrpcCodecDefinition definition : codecDiscoverer.findGrpcCodecs())
//        {
//            if (definition.getCodecType().isForDecompression())
//            {
//                final Codec codec = definition.getCodec();
//                final boolean isAdvertised = definition.isAdvertised();
//                LOG.info(Module.GRPC, "Registering {} decompressor: '{}' ({})",
//                        isAdvertised ? "advertised" : "", codec.getMessageEncoding(), codec.getClass().getName());
//                this.defaultDecompressorRegistry = this.defaultDecompressorRegistry.with(codec, isAdvertised);
//            }
//        }
//        this.decompressionServerConfigurer = builder -> builder.decompressorRegistry(this.defaultDecompressorRegistry);


        DefaultRPCServerChannel instance = DefaultRPCServerChannel.getInstance();
        instance.setPipeline(RPCHandlerManager.getInstance().getPipeline());
        this.dispatcher.setChannel(instance);
        this.dispatcher.initOnce(null);
    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
