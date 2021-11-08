package com.cell.monitor.prometheus.updator.impl;

import com.cell.base.core.annotations.ManagerNode;

import com.cell.monitor.base.executor.IMetricsExecutor;
import com.cell.monitor.base.updator.UpdatorManager;
import com.cell.monitor.prometheus.updator.IPrometheusUpdator;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 17:39
 */
@ManagerNode(group = UpdatorManager.updatorManager, name = "prometheus_updator_manager")
public class DefaultPrometheusUpdator implements IPrometheusUpdator
{
    @Override
    public void updateMetrics(List<IMetricsExecutor> executor)
    {

    }
}
