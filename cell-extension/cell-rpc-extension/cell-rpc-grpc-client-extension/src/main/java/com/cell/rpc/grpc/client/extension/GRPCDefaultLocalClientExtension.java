package com.cell.rpc.grpc.client.extension;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.Plugin;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.grpc.common.constants.GRPCConstants;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.rpc.grpc.client.framework.server.GRPCLocalClientServer;
import com.cell.rpc.grpc.client.framework.server.ILocalGRPCClientServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-01 09:11
 */
@CellOrder(value = OrderConstants.RPC_EXTENSION)
public class GRPCDefaultLocalClientExtension extends AbstractSpringNodeExtension
{
    private ILocalGRPCClientServer clientServer;

    @Plugin
    public ILocalGRPCClientServer clientServer()
    {
        return this.clientServer;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        EventLoopGroup eventLoopGroup = ctx.getEventLoopGroup();
        String grpcPort = ctx.getCommandLine().getOptionValue("grpcPort", GRPCConstants.DEFAULT_GRPC_SERVER_PORT + "");
        String grpcAddr = ctx.getCommandLine().getOptionValue("grpcAddr", GRPCConstants.DEFAULT_GRPC_SERVER_ADDR);
        this.clientServer = new GRPCLocalClientServer(eventLoopGroup, grpcAddr, Integer.parseInt(grpcPort));
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
//        this.clientServer.setStub();
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
