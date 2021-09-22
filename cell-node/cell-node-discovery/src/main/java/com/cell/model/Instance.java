package com.cell.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (!(object instanceof Instance)) return false;
        Instance instance = (Instance) object;
        return getIp().equals(instance.getIp()) &&
                getPort().equals(instance.getPort());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getIp(), getPort());
    }
}
