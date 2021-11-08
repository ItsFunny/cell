package com.cell.rpc.client.base.server;

import com.cell.base.core.concurrent.base.Promise;
import com.cell.dispatcher.IDispatcher;
import com.cell.event.IProcessEvent;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.proxy.IFrameworkProxy;
import com.cell.rpc.grpc.client.framework.annotation.ProxyAnno;
import com.cell.server.abs.AbstractServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 09:25
 */
public abstract class AbstractRPCClientServer extends AbstractServer implements IRPCClientServer
{
    @ProxyAnno(proxyId = 3)
    public static class DisabpleClientProxy implements IFrameworkProxy
    {
        @Override
        public IDispatcher getDispatcher()
        {
            throw new RuntimeException("not  supposed ");
        }

        @Override
        public void proxy(IProcessEvent event, Promise<Object> promise)
        {
            throw new RuntimeException("not  supposed ");
        }

        @Override
        public void proxy(IProcessEvent event)
        {
            throw new RuntimeException("not  supposed ");
        }
    }

    public AbstractRPCClientServer()
    {
        super(new DisabpleClientProxy());
    }

    @Override
    public void serve(IServerRequest request, IServerResponse response)
    {
        throw new RuntimeException("not supposed");
    }
}
