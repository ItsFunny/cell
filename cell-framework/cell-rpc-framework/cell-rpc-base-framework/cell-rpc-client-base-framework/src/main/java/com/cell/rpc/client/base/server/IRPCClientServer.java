package com.cell.rpc.client.base.server;

import com.cell.concurrent.base.Future;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.serialize.ISerializable;
import com.cell.server.IServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 09:23
 */
public interface IRPCClientServer extends IServer
{
    Future<Object> call(IBuzzContext ctx, ISerializable req);

}
