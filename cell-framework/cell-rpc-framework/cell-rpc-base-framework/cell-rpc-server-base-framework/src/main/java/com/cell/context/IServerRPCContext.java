package com.cell.context;

import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.IContext;

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