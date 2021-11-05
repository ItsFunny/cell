package com.cell.discovery.nacos.discovery;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 10:23
 */
@FunctionalInterface
public interface IInstanceOnChange
{
    void onChange(Map<String, List<ServerCmdMetaInfo>> metas);
}
