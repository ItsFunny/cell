package com.cell.rpc.grpc.client.framework.proxy;

import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.base.framework.proxy.AbstractBaseFrameworkProxy;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:11
 */
public class DefaultGRPCClientProxy extends AbstractBaseFrameworkProxy
{
    public DefaultGRPCClientProxy(IDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    protected void afterProxy(IServerRequest request, IServerResponse response, Promise<Object> promise)
    {

    }
}
