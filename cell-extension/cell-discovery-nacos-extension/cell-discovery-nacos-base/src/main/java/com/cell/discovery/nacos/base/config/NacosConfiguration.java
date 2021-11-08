package com.cell.discovery.nacos.base.config;


import com.cell.base.core.annotations.ActiveConfiguration;
import com.cell.base.core.annotations.Plugin;
import com.cell.bee.lb.ILoadBalancerStrategy;
import com.cell.bee.lb.impl.DefaultWeightRoubineStrategy;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 11:11
 */
@ActiveConfiguration
public class NacosConfiguration
{
    @Plugin
    public ILoadBalancerStrategy defaultLoadBalancerStrategy()
    {
        return new DefaultWeightRoubineStrategy();
    }

}
