package com.cell.lb.impl;


import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.lb.ILoadBalancerStrategy;
import com.cell.utils.RandomUtils;

import java.util.Collection;

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
    public ServerCmdMetaInfo choseServer(Collection<ServerCmdMetaInfo> servers, String protocol)
    {
        if (servers == null || servers.size() == 0) return null;
        // FIXME
//        Collections.shuffle(new ArrayList<>(servers));
        int i = RandomUtils.randomInt(0, servers.size());
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
