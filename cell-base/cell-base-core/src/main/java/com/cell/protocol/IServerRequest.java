package com.cell.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:19
 */
public interface IServerRequest
{
    int getContentLength();

    String getHeader(String name);

    //    CommandProtocolID getProtocolId();
    InputStream getInputStream() throws IOException;
}
