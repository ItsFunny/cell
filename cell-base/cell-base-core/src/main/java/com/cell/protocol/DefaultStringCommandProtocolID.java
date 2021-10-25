package com.cell.protocol;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 17:06
 */
@Data
public class DefaultStringCommandProtocolID implements CommandProtocolID
{
    private String multiAddr;

    public DefaultStringCommandProtocolID(String multiAddr)
    {
        this.multiAddr = multiAddr;
    }

    @Override
    public String id()
    {
        return this.multiAddr;
    }
}
