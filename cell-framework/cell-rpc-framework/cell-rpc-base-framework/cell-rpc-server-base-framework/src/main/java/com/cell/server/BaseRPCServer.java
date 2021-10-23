package com.cell.server;

import com.cell.context.InitCTX;
import com.cell.proxy.IRPCServerProxy;
import com.cell.server.abs.AbstractServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:23
 */
public class BaseRPCServer extends AbstractServer implements IRPCServer
{
    public BaseRPCServer(IRPCServerProxy proxy)
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
