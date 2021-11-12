package com.cell.bee.lb.impl;


import com.cell.bee.lb.ILoadBalancerStrategy;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;

import java.util.ArrayList;
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
    // no lock,no atomic
    @Override
    public ServerCmdMetaInfo choseServer(Collection<ServerCmdMetaInfo> servers, String protocol)
    {
        if (servers == null || servers.size() == 0) return null;
        // FIXME
        ArrayList<ServerCmdMetaInfo> serverCmdMetaInfos = new ArrayList<>(servers);
        ServerCmdMetaInfo ret = serverCmdMetaInfos.get(0);
        int minPower = ret.getVotePower();
        for (int i = 1; i < serverCmdMetaInfos.size(); i++)
        {
            ServerCmdMetaInfo serverCmdMetaInfo = serverCmdMetaInfos.get(i);
            if (serverCmdMetaInfo.getVotePower() < minPower)
            {
                ret = serverCmdMetaInfo;
                minPower = serverCmdMetaInfo.getVotePower();
            }
        }
        ret.setVotePower(++minPower);
        return ret;
    }
}
