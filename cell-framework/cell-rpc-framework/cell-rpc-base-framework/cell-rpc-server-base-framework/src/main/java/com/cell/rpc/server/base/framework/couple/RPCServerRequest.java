package com.cell.rpc.server.base.framework.couple;

import com.cell.base.core.protocol.CommandProtocolID;
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
public class RPCServerRequest implements IRPCServerRequest
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
        return this.header.get(name);
    }


    @Override
    public InputStream getInputStream() throws IOException
    {
        return this.requestStream;
    }

    @Override
    public String getProtocol()
    {
        return this.protocolId.id();
    }
}
