package com.cell.proxy;

import com.cell.concurrent.base.Promise;
import com.cell.dispatcher.IDispatcher;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;

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
