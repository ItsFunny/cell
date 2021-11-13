package com.cell.bee.loadbalance.strategy;

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
 * @Date 创建时间：2021-11-13 08:52
 */
@ActiveConfiguration
public class StrategyConfiguration
{
    @Plugin
    public ILoadBalancerStrategy strategy()
    {
        return new DefaultWeightRoubineStrategy();
    }
}
