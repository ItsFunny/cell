package com.cell.http.gate.wrapper;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
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

    public void fillWithMeta(ServerCmdMetaInfo info)
    {
        this.ip = info.getPublicAddress();
        this.port = info.getPublicPort();
        this.serviceName = info.getServiceName();
        this.module = info.getModule();
    }
}
