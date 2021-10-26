package com.cell.server;

import com.cell.context.InitCTX;
import com.cell.couple.HttpRequestWrapper;
import com.cell.couple.HttpResponseWrapper;
import com.cell.proxy.IFrameworkProxy;
import com.cell.proxy.IHttpProxy;
import com.cell.server.abs.AbstractServer;
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
    private short port = 8081;
    private volatile boolean ready;

    public DefaultHttpServer(IFrameworkProxy proxy)
    {
        super(proxy);
    }

    @Override
    public short getPort()
    {
        return this.port;
    }

    @Override
    public boolean ready()
    {
        return this.ready;
    }

    @Override
    public void setPort(short port)
    {
        this.port = port;
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
