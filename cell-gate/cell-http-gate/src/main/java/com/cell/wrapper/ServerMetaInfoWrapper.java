package com.cell.wrapper;

import com.cell.bee.loadbalance.model.ServerMetaInfo;
import lombok.Data;

import java.net.URI;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 15:31
 */
@Data
public class ServerMetaInfoWrapper
{
    private String ip;
    private short port;
    private String serviceName;
    private String module;
    private URI uri;
    private String method;

    public void fillWithMeta(ServerMetaInfo info)
    {
        this.ip = info.getIp();
        this.port = info.getPort();
        this.serviceName = info.getServiceName();
        this.module=info.getModule();
    }
}
