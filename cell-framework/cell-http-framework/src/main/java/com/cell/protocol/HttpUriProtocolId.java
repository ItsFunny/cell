package com.cell.protocol;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 01:06
 */
public class HttpUriProtocolId implements CommandProtocolID
{
    private String uri;

    public HttpUriProtocolId(String uri)
    {
        this.uri = uri;
    }

    @Override
    public String id()
    {
        return this.uri;
    }
}
