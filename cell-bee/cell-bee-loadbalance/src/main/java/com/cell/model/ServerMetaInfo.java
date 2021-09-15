package com.cell.model;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-09 10:45
 */
@Data
public class ServerMetaInfo
{
    private String serviceName;
    private String ip;
    private short port;
    private String module;

    private boolean enable;
    private boolean healthy;
}
