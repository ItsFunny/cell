package com.cell.couple;

import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.IServerRequest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 13:17
 */
public class RPCServerRequest implements IServerRequest
{
    private CommandProtocolID protocolId;
    private InputStream requestStream;
    private int requestSize;

    @Override
    public int getContentLength()
    {
        return this.requestSize;
    }

    @Override
    public CommandProtocolID getProtocolId()
    {
        return this.protocolId;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return this.requestStream;
    }
}
