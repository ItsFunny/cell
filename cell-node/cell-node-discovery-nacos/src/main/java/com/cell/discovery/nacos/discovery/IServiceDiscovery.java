package com.cell.discovery.nacos.discovery;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.config.IInitOnce;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 21:03
 */
public interface IServiceDiscovery extends IInitOnce
{
    Map<String, List<ServerCmdMetaInfo>> getServerMetas();

    void setCluster(String cluster);

    ServerCmdMetaInfo choseServer(String method, String protocol);
}
