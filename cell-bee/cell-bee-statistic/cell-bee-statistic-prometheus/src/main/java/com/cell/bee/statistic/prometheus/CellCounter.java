package com.cell.bee.statistic.prometheus;

import io.prometheus.client.Collector;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-28 05:25
 */
public class CellCounter extends BaseCollector<CellGaugeStator.Child> implements Collector.Describable
{

    protected CellCounter(Builder<?, ?> b)
    {
        super(b);
    }

    @Override
    public List<MetricFamilySamples> describe()
    {
        return null;
    }

    @Override
    protected CellGaugeStator.Child newChild()
    {
        return null;
    }

    @Override
    public List<MetricFamilySamples> collect()
    {
        return null;
    }
}
