package com.cell.lb;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;

import java.util.Collection;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 06:10
 */
public interface ILoadBalancerStrategy
{
    ServerCmdMetaInfo choseServer(Collection<ServerCmdMetaInfo> servers, String protocol);
}
