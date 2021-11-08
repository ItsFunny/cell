package com.cell.node.discovery.nacos.discovery;

import com.cell.base.common.context.IInitOnce;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;

import java.util.Map;
import java.util.Set;

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
    Map<String, Set<ServerCmdMetaInfo>> getServerMetas();

    void setCluster(String cluster);

    ServerCmdMetaInfo choseServer(String method, String protocol);
}
