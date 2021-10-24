package com.cell.protocol.impl;

import com.cell.protocol.Summary;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-24 13:05
 */
@Data
public class BaseSummary implements Summary
{
    private String requestIP;
    private String protocolId;
    private long receiveTimestamp;
    private String token;
    private String sequenceId;
    private long timeOut;

    @Override
    public String getProtocolId()
    {
        return this.protocolId;
    }

}