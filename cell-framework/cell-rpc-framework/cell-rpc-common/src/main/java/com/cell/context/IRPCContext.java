package com.cell.context;

import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.IBuzzContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-20 22:00
 */
public interface IRPCContext extends IBuzzContext
{
    CommandProtocolID getProtocolID();

//    void response(RPCContextResponseWrapper wp);

}
