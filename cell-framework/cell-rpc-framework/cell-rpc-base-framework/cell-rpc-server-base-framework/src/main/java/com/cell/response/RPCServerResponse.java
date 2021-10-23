package com.cell.response;

import com.cell.protocol.IServerResponse;
import com.cell.protocol.impl.AbstractBaseResponse;

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
public class RPCServerResponse extends AbstractBaseResponse implements IServerResponse
{
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


}
