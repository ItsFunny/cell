package com.cell.server;

import com.cell.context.InitCTX;
import com.cell.proxy.IFrameworkProxy;
import com.cell.server.abs.AbstractServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 15:37
 */
public class GRPCClientServer extends AbstractServer
{

    public GRPCClientServer(IFrameworkProxy proxy)
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
