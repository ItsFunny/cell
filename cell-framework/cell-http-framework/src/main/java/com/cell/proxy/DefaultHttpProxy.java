package com.cell.proxy;

import com.cell.annotation.ProxyAnno;
import com.cell.concurrent.base.Promise;
import com.cell.dispatcher.IHttpDispatcher;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:33
 */
@ProxyAnno(proxyId = 1)
public class DefaultHttpProxy extends AbstractBaseFrameworkProxy implements IHttpProxy
{
    public DefaultHttpProxy(IHttpDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    protected void afterProxy(IServerRequest request, IServerResponse response, Promise<Object> promise)
    {

    }
}
