package com.cell.rpc.client.base.framework.server;

import com.cell.base.core.concurrent.base.Future;
import com.cell.base.core.protocol.IBuzzContext;
import com.cell.base.core.serialize.ISerializable;
import com.cell.base.framework.server.IServer;

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
