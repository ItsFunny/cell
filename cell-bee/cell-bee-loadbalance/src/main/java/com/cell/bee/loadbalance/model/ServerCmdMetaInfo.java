package com.cell.bee.loadbalance.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:31
 */
@Data
public class ServerCmdMetaInfo extends ServerMetaInfo
{
    private int id;
    private String protocol;
    protected String module;

    public static ServerCmdMetaInfo fromServerMetaInfo(ServerMetaInfo info, String protocol, String module)
    {
        ServerCmdMetaInfo ret = new ServerCmdMetaInfo();
        // FIXME
        BeanUtils.copyProperties(info, ret);
        ret.protocol = protocol;
        ret.module = module;
        return ret;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (!(object instanceof ServerCmdMetaInfo)) return false;
        ServerCmdMetaInfo that = (ServerCmdMetaInfo) object;
        return this.ID() - that.ID() == 0;
    }

    @Override
    public int hashCode()
    {
        return this.ID();
    }

    public int ID()
    {
        if (this.id == 0)
        {
            this.id = Objects.hash(getServiceName(), getProtocol(), getIp(), getPort(), getModule(), getMetaData());
        }
        return this.id;
    }

    @Override
    public String toString()
    {
        return "ServerCmdMetaInfo{" +
                "protocol='" + protocol + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", module='" + module + '\'' +
                ", metaData=" + metaData +
                ", enable=" + enable +
                ", healthy=" + healthy +
                '}';
    }
}
