package com.cell.lb.impl;


import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.lb.ILoadBalancerStrategy;

import java.util.Collections;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-09 22:37
 */
// FIXME ,its not weight base
public class DefaultWeightRoubineStrategy implements ILoadBalancerStrategy
{
    @Override
    public ServerCmdMetaInfo choseServer(List<ServerCmdMetaInfo> servers, String protocol)
    {
        if (servers == null || servers.size() == 0) return null;

        Collections.shuffle(servers);
        for (ServerCmdMetaInfo server : servers)
        {
            if (server.isEnable() && server.isHealthy())
            {
                return server;
            }
        }
        return null;
    }

}
