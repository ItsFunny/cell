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

    private int id;


    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (!(object instanceof ServerMetaInfo)) return false;
        ServerMetaInfo that = (ServerMetaInfo) object;
        return getPort() == that.getPort() &&
                Objects.equals(getServiceName(), that.getServiceName()) &&
                Objects.equals(getIp(), that.getIp()) &&
                Objects.equals(getModule(), that.getModule()) &&
                Objects.equals(getMetaData(), that.getMetaData());
    }

    public int ID()
    {
        if (this.id == 0)
        {
            this.id = Objects.hash(getServiceName(), getIp(), getPort(), getModule(), getMetaData());
        }
        return this.id;
    }

}
