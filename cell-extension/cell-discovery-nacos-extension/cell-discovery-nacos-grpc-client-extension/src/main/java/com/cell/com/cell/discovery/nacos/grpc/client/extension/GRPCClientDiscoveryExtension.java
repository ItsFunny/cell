package com.cell.com.cell.discovery.nacos.grpc.client.extension;

import com.cell.annotations.CellOrder;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.extension.AbstractSpringNodeExtension;

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
