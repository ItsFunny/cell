package com.cell.couple;

import com.cell.base.core.protocol.CommandProtocolID;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 00:20
 */
@Data
public class HttpRequestWrapper implements IHttpServerRequest
{
    private HttpServletRequest request;
    private CommandProtocolID protocolID;

    public HttpRequestWrapper(HttpServletRequest request)
    {
        this.request = request;
    }

    @Override
    public int getContentLength()
    {
        return this.request.getContentLength();
    }

    @Override
    public String getHeader(String name)
    {
        return this.request.getHeader(name);
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        return this.request.getInputStream();
    }

    @Override
    public HttpServletRequest getInternalRequest()
    {
        return this.request;
    }
}
