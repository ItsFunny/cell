package com.cell.node.discovery.nacos.discovery;

import com.cell.base.common.context.IInitOnce;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.node.discovery.nacos.discovery.abs.InstanceWrapper;

import java.util.List;
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

    Map<String, List<InstanceWrapper>> getCurrentInstances();

    // 判断是否需要更新服务信息
    void transferIfNeed();
}
