package com.cell.updator.impl;

import com.cell.annotations.ManagerNode;
import com.cell.executor.IMetricsExecutor;
import com.cell.updator.IPrometheusUpdator;
import com.cell.updator.UpdatorManager;

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
