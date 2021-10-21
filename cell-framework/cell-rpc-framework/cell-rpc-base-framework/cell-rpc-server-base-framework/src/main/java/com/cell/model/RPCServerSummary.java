package com.cell.model;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:55
 */
@Data
public class RPCServerSummary
{
    private String requestIP;
    private String requestUrl;
    private long receiveTimestamp;
    private String token;
    private String platform;
    private String versionInt;
    private String version;
    private String systemVersionInt;
    private String systemVersion;
    private String systemModel;
    private String systemBrank;
    private String deviceId;
    private String networkType;
    private String sequenceId;

    private long timeOut;

}
