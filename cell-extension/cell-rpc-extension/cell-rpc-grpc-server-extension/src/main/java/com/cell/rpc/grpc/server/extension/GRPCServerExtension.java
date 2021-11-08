package com.cell.rpc.grpc.server.extension;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.Plugin;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.grpc.common.config.GRPCServerConfiguration;
import com.cell.grpc.server.framework.server.DefaultGRPServer;
import com.cell.grpc.server.framework.server.IGRPCServer;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.proxy.IProxy;
import com.cell.base.framework.root.Root;
import com.cell.rpc.common.manager.RPCHandlerManager;
import com.cell.rpc.server.base.framework.channel.DefaultRPCServerChannel;
import com.cell.rpc.server.base.framework.dispatcher.impl.DefaultRPCServerCommandDispatcher;
import com.cell.rpc.server.base.framework.proxy.DefaultRPCServerProxy;
import com.cell.rpc.server.base.framework.proxy.IRPCServerProxy;
import com.cell.sdk.configuration.Configuration;
import com.cell.sdk.log.LOG;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
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
@CellOrder(value = OrderConstants.RPC_EXTENSION)
public class GRPCServerExtension extends AbstractSpringNodeExtension
{
    private static final String moduleName = "env.shared.rpc.grpc.json";

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
    public Options getOptions()
    {
        // TODO ,not here
        Options ret = new Options();
        ret.addOption("grpcPort", true, "grpc 端口号");
        return ret;
    }

    @Override
    public Object loadConfiguration(INodeContext ctx) throws Exception
    {
        GRPCServerConfiguration configuration = null;
        try
        {
            configuration = Configuration.getDefault().getConfigValue(moduleName).asObject(GRPCServerConfiguration.class);
            return configuration;
        } catch (Exception e)
        {
            configuration = GRPCServerConfiguration.defaultConfiguration();
            return configuration;
        } finally
        {
        }
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        CommandLine cmd = ctx.getCommandLine();
        this.dispatcher = new DefaultRPCServerCommandDispatcher();
        this.proxy = new DefaultRPCServerProxy(this.dispatcher);
        this.server = new DefaultGRPServer(this.proxy);

        String port = cmd.getOptionValue("grpcPort");
        if (!StringUtils.isEmpty(port))
        {
            this.server.setPort(Short.valueOf(port));
        }
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
