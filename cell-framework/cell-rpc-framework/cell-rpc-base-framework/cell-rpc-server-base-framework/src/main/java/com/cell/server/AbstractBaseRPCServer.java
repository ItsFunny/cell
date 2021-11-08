package com.cell.server;

import com.cell.proxy.IRPCServerProxy;
import com.cell.server.abs.AbstractServer;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:23
 */
@Data
public abstract class AbstractBaseRPCServer extends AbstractServer implements IRPCServer
{


    public AbstractBaseRPCServer(IRPCServerProxy proxy)
    {
        super(proxy);
    }

    @Override
    protected void onShutdown()
    {

    }

}
