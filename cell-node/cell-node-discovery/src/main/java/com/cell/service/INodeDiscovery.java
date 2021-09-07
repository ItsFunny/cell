package com.cell.service;

import com.cell.model.Instance;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-07 21:55
 */
public interface INodeDiscovery
{
    List<Instance> getServerInstanceList();

    void registerServerInstance(Instance instance);
}
