package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.context.INodeContext;
import com.cell.services.IGRPCClientService;
import com.cell.services.impl.GRPCClientServiceImpl;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-01 09:11
 */
public class GRPCClientExtension extends AbstractSpringNodeExtension
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
