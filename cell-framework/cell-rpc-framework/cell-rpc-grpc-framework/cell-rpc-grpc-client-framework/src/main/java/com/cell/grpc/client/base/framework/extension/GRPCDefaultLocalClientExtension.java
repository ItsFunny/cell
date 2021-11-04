package com.cell.grpc.client.base.framework.extension;

import com.cell.annotations.Plugin;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.context.INodeContext;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.extension.ConcurrentExtension;
import com.cell.grpc.client.base.framework.services.IGRPCClientService;
import com.cell.grpc.client.base.framework.services.impl.GRPCClientServiceImpl;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-01 09:11
 */
public class GRPCDefaultLocalClientExtension extends AbstractSpringNodeExtension
{

    private IGRPCClientService grpcClientService;

    @Plugin
    public IGRPCClientService service()
    {
        return this.grpcClientService;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        EventLoopGroup eventLoopGroup = ConcurrentExtension.getEventLoopGroup();
        this.grpcClientService = new GRPCClientServiceImpl(eventLoopGroup);
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
