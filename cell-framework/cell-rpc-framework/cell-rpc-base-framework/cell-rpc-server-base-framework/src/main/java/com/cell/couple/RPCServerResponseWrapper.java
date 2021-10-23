package com.cell.couple;

import com.cell.constants.ContextConstants;
import com.cell.protocol.impl.AbstractBaseResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:25
 */
public class RPCServerResponseWrapper extends AbstractBaseResponse implements IRPCServerResponse
{
    private Map<String, String> header = null;
    private long status = ContextConstants.SUCCESS;

    @Override
    public void setHeader(String name, String value)
    {
        this.check();
        this.header.put(name, value);
    }

    private void check()
    {
        if (this.header == null)
        {
            this.header = new HashMap<>();
        }
    }

    @Override
    public void setStatus(long sc)
    {
        this.status = sc;
    }

    @Override
    public void addHeader(String name, String value)
    {
        this.check();
        this.header.put(name, value);
    }

    @Override
    public Object getResponse()
    {
        return this.getPromise();
    }
}
