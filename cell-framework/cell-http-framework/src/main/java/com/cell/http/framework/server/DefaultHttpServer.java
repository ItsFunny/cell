package com.cell.http.framework.server;

import com.cell.base.common.context.InitCTX;
import com.cell.base.framework.proxy.IFrameworkProxy;
import com.cell.base.framework.server.abs.AbstractServer;
import com.cell.http.framework.couple.HttpRequestWrapper;
import com.cell.http.framework.couple.HttpResponseWrapper;
import com.cell.http.framework.proxy.IHttpProxy;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:32
 */
public class DefaultHttpServer extends AbstractServer implements IHttpServer
{
    private volatile boolean ready;

    public DefaultHttpServer(IFrameworkProxy proxy)
    {
        super(proxy);
        this.setPort((short) 8081);
    }

    @Override
    public boolean ready()
    {
        return this.ready;
    }


    @Override
    protected void onStart()
    {
        this.ready = true;
    }

    @Override
    public IHttpProxy getHttpProxy()
    {
        return (IHttpProxy) this.getProxy();
    }

    @Override
    @ResponseBody
    public DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response)
    {
        if (!this.ready)
        {
            DeferredResult<Object> ret = new DeferredResult<>();
            ret.setResult("not ready yet");
            return ret;
        }
        HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(response);
        this.serve(new HttpRequestWrapper(request), httpResponseWrapper);
        return (DeferredResult<Object>) httpResponseWrapper.getResponse();
    }


    @Override
    protected void onInit(InitCTX ctx)
    {

    }


    @Override
    protected void onShutdown()
    {

    }
}
