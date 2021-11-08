package com.cell.rpc.server.base.framework.context;

import com.cell.base.core.protocol.CommandProtocolID;
import com.cell.base.core.protocol.IContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-20 22:00
 */
public interface IServerRPCContext extends IContext
{
    CommandProtocolID getProtocolID();

//    void response(RPCContextResponseWrapper wp);

}
