package com.cell.proxy;

import com.cell.annotation.ProxyAnno;
import com.cell.concurrent.base.Promise;
import com.cell.dispatcher.IDispatcher;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.proxy.abs.AbstractRPCProxy;

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
