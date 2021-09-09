package com.cell.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-07 21:55
 */
@Data
@Builder
public class Instance
{
    private String ip;
    private Integer port;
    private String serviceName;
    private byte weight = 1;
    private String clusterName;
    private Map<String, String> metaData;

    private boolean healthy = true;
    private boolean enable = true;
}
