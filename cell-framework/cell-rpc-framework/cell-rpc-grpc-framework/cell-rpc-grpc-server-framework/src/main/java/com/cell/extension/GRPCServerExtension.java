package com.cell.extension;

import com.cell.context.INodeContext;

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

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

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
