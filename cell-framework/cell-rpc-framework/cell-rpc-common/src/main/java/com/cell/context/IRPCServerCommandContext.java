package com.cell.context;

import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.IServerRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:16
 */
public interface IRPCServerCommandContext extends  IRPCContext
{
    IServerRequest getRequest();
    void response(ContextResponseWrapper wp);
}
