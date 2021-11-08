package com.cell.rpc.server.base.framework.proxy;

import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.rpc.common.proxy.abs.AbstractRPCProxy;
import com.cell.rpc.grpc.client.framework.annotation.ProxyAnno;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:00
 */
@ProxyAnno(proxyId = 2)
public class DefaultRPCServerProxy extends AbstractRPCProxy implements IRPCServerProxy
{
    public DefaultRPCServerProxy(IDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    protected void afterProxy(IServerRequest request, IServerResponse response, Promise<Object> promise)
    {
    }
}
