package com.cell.bee.lb.impl;

import com.cell.base.common.utils.RandomUtils;
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
 * @Date 创建时间：2021-11-12 09:48
 */
public class DefaultRandomStrategy implements ILoadBalancerStrategy
{
    @Override
    public ServerCmdMetaInfo choseServer(Collection<ServerCmdMetaInfo> servers, String protocol)
    {
        if (servers == null || servers.size() == 0) return null;
        ArrayList<ServerCmdMetaInfo> serverCmdMetaInfos = new ArrayList<>(servers);
        if (servers.size() == 1) return serverCmdMetaInfos.get(0);
        int i = RandomUtils.randomInt(0, serverCmdMetaInfos.size());
        return serverCmdMetaInfos.get(i);
    }
}
