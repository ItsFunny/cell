package com.cell.rpc.client.base.framework.server;

import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.base.framework.proxy.IFrameworkProxy;
import com.cell.base.framework.server.abs.AbstractServer;
import com.cell.event.IProcessEvent;
import com.cell.rpc.grpc.client.framework.annotation.ProxyAnno;

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
    @Override
    public byte type()
    {
        throw new ProgramaException("not supposed");
    }

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
