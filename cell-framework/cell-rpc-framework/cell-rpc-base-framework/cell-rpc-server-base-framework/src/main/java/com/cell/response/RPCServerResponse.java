package com.cell.response;

import com.cell.concurrent.base.Promise;
import com.cell.protocol.IServerResponse;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, String> header;
    private long status;
    private volatile boolean expired;
    private Promise<Object> promise;

    private void checkHeader()
    {
        if (this.header == null)
        {
            this.header = new HashMap<>();
        }
    }

    @Override
    public void setPromise(Promise<Object> promise)
    {
        this.promise = promise;
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
        promise.trySuccess(o);
    }

    @Override
    public boolean isSetOrExpired()
    {
        return promise.isDone() || !promise.isCancelled();
    }
}
