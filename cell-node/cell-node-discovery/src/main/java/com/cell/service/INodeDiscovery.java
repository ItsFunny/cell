package com.cell.service;

import com.cell.base.core.config.IInitOnce;
import com.cell.base.core.filters.ISimpleFilter;
import com.cell.model.Instance;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-07 21:55
 */
public interface INodeDiscovery extends IInitOnce
{
    // key: serviceName
    Map<String, List<Instance>> getServerInstanceList(String cluster, ISimpleFilter<Instance>... filters);

    List<String> getAllServices();

    List<Instance> getServiceAllInstance(String serviceName, ISimpleFilter<Instance>... filters);

    void registerServerInstance(Instance instance);
}
