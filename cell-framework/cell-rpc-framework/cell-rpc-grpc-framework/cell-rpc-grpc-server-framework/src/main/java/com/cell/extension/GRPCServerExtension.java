package com.cell.extension;

import com.cell.Configuration;
import com.cell.annotations.Plugin;
import com.cell.channel.DefaultRPCServerChannel;
import com.cell.discovery.nacos.config.GRPCServerConfiguration;
import com.cell.constants.ProtocolConstants;
import com.cell.context.INodeContext;
import com.cell.dispatcher.IDispatcher;
import com.cell.dispatcher.impl.DefaultRPCServerCommandDispatcher;
import com.cell.log.LOG;
import com.cell.manager.RPCHandlerManager;
import com.cell.models.Module;
import com.cell.proxy.DefaultRPCServerProxy;
import com.cell.proxy.IProxy;
import com.cell.proxy.IRPCServerProxy;
import com.cell.reactor.ICommandReactor;
import com.cell.root.Root;
import com.cell.server.DefaultGRPServer;
import com.cell.server.IGRPCServer;
import org.springframework.context.annotation.Bean;

import java.util.Set;

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
        Set<ICommandReactor> reactors = Root.getInstance().getReactor(ProtocolConstants.REACTOR_TYPE_RPC_GRPC_SERVER);
        for (ICommandReactor reactor : reactors)
        {
            LOG.info(Module.GRPC_SERVER, "添加http Reactor,info:{}", reactor);
            this.dispatcher.addReactor(reactor);
        }
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
