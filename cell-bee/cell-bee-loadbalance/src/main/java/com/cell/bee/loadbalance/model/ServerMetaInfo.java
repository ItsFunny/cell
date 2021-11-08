package com.cell.bee.loadbalance.model;

import com.cell.transport.model.ServerMetaData;
import lombok.Data;

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
    protected String serviceName;
    protected String ip;
    protected short port;

    protected ServerMetaData metaData;

    protected boolean enable;
    protected boolean healthy;


    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (!(object instanceof ServerMetaInfo)) return false;
        ServerMetaInfo that = (ServerMetaInfo) object;
        return getPort() == that.getPort() &&
                Objects.equals(getServiceName(), that.getServiceName()) &&
                Objects.equals(getIp(), that.getIp()) &&
                Objects.equals(getMetaData(), that.getMetaData());
    }

    @Override
    public String toString()
    {
        return "ServerMetaInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", metaData=" + metaData +
                ", enable=" + enable +
                ", healthy=" + healthy +
                '}';
    }
}
