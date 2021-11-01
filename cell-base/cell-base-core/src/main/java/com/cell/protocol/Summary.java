package com.cell.protocol;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-24 11:18
 */
public interface Summary
{
    String getRequestIP();


    String getProtocolId();

    long getReceiveTimestamp();

    String getToken();

    void setSequenceId(String id);

    String getSequenceId();

    long getTimeOut();
}
