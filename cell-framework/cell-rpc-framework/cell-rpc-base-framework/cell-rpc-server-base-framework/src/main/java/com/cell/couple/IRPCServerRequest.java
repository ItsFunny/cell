package com.cell.couple;

import com.cell.base.core.protocol.IServerRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 13:14
 */
public interface IRPCServerRequest extends IServerRequest
{
    String getProtocol();
}
