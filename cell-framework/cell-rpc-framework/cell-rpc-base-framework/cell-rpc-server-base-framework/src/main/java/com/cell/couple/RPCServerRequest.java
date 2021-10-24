package com.cell.couple;

import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.IServerRequest;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 13:17
 */
@Data
public class RPCServerRequest implements IServerRequest
{
    private CommandProtocolID protocolId;
    private InputStream requestStream;
    private int requestSize;
    private Map<String, String> header = new HashMap<>(1);

    @Override
    public int getContentLength()
    {
        return this.requestSize;
    }

    @Override
    public String getHeader(String name)
    {
        return this.getHeader(name);
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        return this.requestStream;
    }
}
