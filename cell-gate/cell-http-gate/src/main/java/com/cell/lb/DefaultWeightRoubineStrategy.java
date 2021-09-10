package com.cell.lb;

import com.cell.annotations.ActivePlugin;
import com.cell.model.ServerMetaInfo;

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
    public ServerMetaInfo choseServer(List<ServerMetaInfo> servers, String uri)
    {
        if (servers == null || servers.size() == 0) return null;

        Collections.shuffle(servers);
        for (ServerMetaInfo server : servers)
        {
            if (server.isEnable() && server.isHealthy())
            {
                return server;
            }
        }
        return null;
    }
}
