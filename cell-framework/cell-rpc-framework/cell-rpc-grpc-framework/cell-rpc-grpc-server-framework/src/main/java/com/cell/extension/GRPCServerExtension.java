package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.channel.DefaultRPCServerChannel;
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
    private static final String grpcServerConfigModule = "env.shared.rpc.grpc.json";

    private IGRPCServer server;
    private IDispatcher dispatcher;
    private IRPCServerProxy proxy;

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
    protected void onInit(INodeContext ctx) throws Exception
    {
        this.dispatcher = new DefaultRPCServerCommandDispatcher();
        this.proxy = new DefaultRPCServerProxy(this.dispatcher);
        this.server = new DefaultGRPServer(this.proxy);
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
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
