package com.cell.discovery.services;

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
 * @Date 创建时间：2021-11-05 09:47
 */
public interface IDiscoveryClientService extends IInitOnce
{
    void setCluster(String cluster);

    Map<String, List<ServerCmdMetaInfo>> getServerMetas();

    ServerCmdMetaInfo choseServer(String protocol, String... values);

    void onChange(Runnable callBack);
}
