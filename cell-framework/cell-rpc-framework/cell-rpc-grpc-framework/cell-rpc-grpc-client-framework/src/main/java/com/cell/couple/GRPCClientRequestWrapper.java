package com.cell.couple;

import com.cell.base.core.protocol.IServerRequest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:23
 */
public class GRPCClientRequestWrapper implements IServerRequest
{

    @Override
    public int getContentLength()
    {
        return 0;
    }

    @Override
    public String getHeader(String name)
    {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return null;
    }
}
