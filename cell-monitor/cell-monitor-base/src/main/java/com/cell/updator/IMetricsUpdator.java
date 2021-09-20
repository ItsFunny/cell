package com.cell.updator;

import com.cell.executor.IMetricsExecutor;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 17:38
 */
public interface IMetricsUpdator
{
    void updateMetrics(List<IMetricsExecutor> executor);
}
