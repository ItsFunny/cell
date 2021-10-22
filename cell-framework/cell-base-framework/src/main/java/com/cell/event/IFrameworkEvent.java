package com.cell.event;

import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 19:00
 */
public interface IFrameworkEvent extends IProcessEvent
{
    IServerRequest getRequest();

    IServerResponse getResponse();
}
