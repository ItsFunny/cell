package com.cell.discovery.nacos.grpc.client.extension;

import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.Plugin;
import com.cell.base.common.constants.CommandLineConstants;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.common.constants.OrderConstants;
import com.cell.discovery.nacos.grpc.client.extension.discovery.GRPCClientServiceDiscovery;
import com.cell.discovery.nacos.grpc.client.extension.server.DefaultGrpcNacosClientServer;
import com.cell.discovery.nacos.grpc.client.extension.server.IGRPCNacosClientServer;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 05:48
 */
@CellOrder(value = OrderConstants.RPC_CLIENT_NACOS_DISCOVERY_EXTENSION)
public class GRPCClientDiscoveryExtension extends AbstractSpringNodeExtension
{
    private IGRPCNacosClientServer nacosClientServer;

    private GRPCClientServiceDiscovery serviceDiscovery;

    @Plugin
    public IGRPCNacosClientServer nacosClientServer()
    {
        return this.nacosClientServer;
    }

    @Plugin
    public GRPCClientServiceDiscovery discovery()
    {
        return this.serviceDiscovery;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        EventLoopGroup eventLoopGroup =ctx.getEventLoopGroup();
        this.nacosClientServer = new DefaultGrpcNacosClientServer(eventLoopGroup);
        this.serviceDiscovery = new GRPCClientServiceDiscovery();
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
        String cluster = ctx.getCommandLine().getOptionValue(CommandLineConstants.CLUSTER);
        cluster = StringUtils.isEmpty(cluster) ? CommandLineConstants.DEFAULT_CLSUTER_VALUE : cluster;
        this.serviceDiscovery.setCluster(cluster);
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
