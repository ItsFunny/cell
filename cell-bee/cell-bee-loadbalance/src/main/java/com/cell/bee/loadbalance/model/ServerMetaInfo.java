package com.cell.bee.loadbalance.model;

import com.cell.bee.transport.model.ServerMetaData;
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
    protected ServerMetaData metaData;
    protected String visualAddress;
    protected Short visualPort;
    protected boolean enable;
    protected boolean healthy;


    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (!(object instanceof ServerMetaInfo)) return false;
        ServerMetaInfo that = (ServerMetaInfo) object;
        return
                Objects.equals(getServiceName(), that.getServiceName()) &&
                        Objects.equals(getMetaData(), that.getMetaData());
    }

    public String getPublicAddress()
    {
        return this.metaData.getExtraInfo().getPublicNetwork().getAddress();
    }

    public short getPublicPort()
    {
        return (short) this.metaData.getExtraInfo().getPublicNetwork().getPort();
    }

    @Override
    public String toString()
    {
        return "ServerMetaInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", metaData=" + metaData +
                ", enable=" + enable +
                ", healthy=" + healthy +
                '}';
    }
}
