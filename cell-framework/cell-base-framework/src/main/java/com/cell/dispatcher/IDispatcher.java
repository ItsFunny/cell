package com.cell.dispatcher;

import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.reactor.ICommandReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 18:39
 */
public interface IDispatcher
{
    void dispatch(IServerRequest request, IServerResponse response);

    void addReactor(ICommandReactor reactor);
}
