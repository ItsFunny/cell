package com.cell.http.framework.couple;

import com.cell.base.core.protocol.impl.AbstractBaseResponse;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 00:32
 */
public class HttpResponseWrapper extends AbstractBaseResponse implements IHttpServerResponse
{
    private HttpServletResponse response;

    private DeferredResult<Object> responseResult;


    public HttpResponseWrapper(HttpServletResponse response)
    {
        this.response = response;
    }

    public void setHeader(String name, String value)
    {
        this.response.addHeader(name, value);
    }

    @Override
    public void setStatus(long sc)
    {
        this.response.setStatus((int) sc);
    }

    @Override
    public void addHeader(String name, String value)
    {
        this.response.addHeader(name, value);
    }

    @Override
    public Object getResponse()
    {
        return this.responseResult;
    }


    @Override
    public HttpServletResponse getInternalResponse()
    {
        return this.response;
    }

    @Override
    public void setDeferredResponse(DeferredResult<Object> response)
    {
        this.responseResult = response;
    }
}
