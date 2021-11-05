package com.cell.bee.loadbalance.model;

import com.cell.transport.model.ServerMetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Objects;

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

    private ServerMetaData metaData;

    private boolean enable;
    private boolean healthy;

}
