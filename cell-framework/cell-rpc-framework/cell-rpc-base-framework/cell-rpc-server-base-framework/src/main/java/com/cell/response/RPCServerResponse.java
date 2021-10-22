package com.cell.response;

import com.cell.protocol.IServerResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 13:18
 */
public class RPCServerResponse implements IServerResponse
{
    private Future<Object> ret;
    private Map<String, String> header;
    private long status;
    private volatile boolean expired;

    private void checkHeader()
    {
        if (this.header == null)
        {
            this.header = new HashMap<>();
        }
    }

    @Override
    public void setHeader(String name, String value)
    {
        this.checkHeader();
        this.header.put(name, value);
    }

    @Override
    public void setStatus(long sc)
    {

    }

    @Override
    public void addHeader(String name, String value)
    {

    }

    @Override
    public void fireResult(Object o)
    {

    }

    @Override
    public boolean isSetOrExpired()
    {
        return ret.isDone() || !ret.isCancelled();
    }
}
