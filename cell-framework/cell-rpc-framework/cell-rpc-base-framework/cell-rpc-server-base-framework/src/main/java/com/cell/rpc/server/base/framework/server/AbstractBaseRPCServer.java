package com.cell.rpc.server.base.framework.server;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.framework.server.abs.AbstractServer;
import com.cell.rpc.server.base.framework.proxy.IRPCServerProxy;
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

    @Override
    public byte type()
    {
        return ProtocolConstants.TYPE_RPC;
    }
}
