package com.cell.rpc.grpc.client.framework.services.impl;

import com.cell.base.core.context.InitCTX;
import com.cell.proxy.IFrameworkProxy;
import com.cell.server.abs.AbstractServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 09:14
 */
public class NacosDiscoveryGRPCClientServer extends AbstractServer
{

    public NacosDiscoveryGRPCClientServer(IFrameworkProxy proxy)
    {
        super(proxy);
    }

    @Override
    protected void onStart()
    {

    }

    @Override
    protected void onShutdown()
    {

    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
