package com.cell.discovery.nacos.grpc.client.extension;

import com.cell.annotations.CellOrder;
import com.cell.annotations.Plugin;
import com.cell.discovery.nacos.grpc.client.server.DefaultGrpcNacosClientServer;
import com.cell.discovery.nacos.grpc.client.server.IGRPCNacosClientServer;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.extension.ConcurrentExtension;

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

    @Plugin
    public IGRPCNacosClientServer nacosClientServer()
    {
        return this.nacosClientServer;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        EventLoopGroup eventLoopGroup = ConcurrentExtension.getEventLoopGroup();
        this.nacosClientServer = new DefaultGrpcNacosClientServer(eventLoopGroup);
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {


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
