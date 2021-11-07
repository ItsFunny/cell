package com.cell.rpc.grpc.server.extension;

import com.cell.Configuration;
import com.cell.annotations.CellOrder;
import com.cell.annotations.Plugin;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.channel.DefaultRPCServerChannel;
import com.cell.com.cell.grpc.common.config.GRPCServerConfiguration;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.dispatcher.IDispatcher;
import com.cell.dispatcher.impl.DefaultRPCServerCommandDispatcher;
import com.cell.http.gate.extension.AbstractSpringNodeExtension;
import com.cell.grpc.server.framework.server.DefaultGRPServer;
import com.cell.grpc.server.framework.server.IGRPCServer;
import com.cell.log.LOG;
import com.cell.manager.RPCHandlerManager;
import com.cell.models.Module;
import com.cell.proxy.DefaultRPCServerProxy;
import com.cell.proxy.IProxy;
import com.cell.proxy.IRPCServerProxy;
import com.cell.reactor.ICommandReactor;
import com.cell.root.Root;
import com.cell.utils.StringUtils;
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
