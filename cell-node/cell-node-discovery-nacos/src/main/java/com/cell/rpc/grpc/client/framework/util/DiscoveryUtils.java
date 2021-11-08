package com.cell.rpc.grpc.client.framework.util;

import com.cell.filters.ISimpleFilter;
import com.cell.model.Instance;
import com.cell.utils.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-09 21:21
 */
public class DiscoveryUtils
{
    public static Map<String, List<Instance>> convNacosMapInstanceToCellInstance(Map<String, List<com.alibaba.nacos.api.naming.pojo.Instance>> m, ISimpleFilter<Instance>... filters)
    {
        Map<String, List<Instance>> ret = new HashMap<>();
        Set<String> names = m.keySet();
        names.stream().forEach(n ->
        {
            List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances = m.get(n);
            if (CollectionUtils.isEmpty(allInstances))
            {
                return;
            }
            List<Instance> instances = convNaocsInstance2CellInstance(allInstances, filters);
            ret.put(n, instances);
        });

        return ret;
    }

    // TODO: 这里
    public static List<Instance> convNaocsInstance2CellInstance(List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances, ISimpleFilter<Instance>... filters)
    {
        return allInstances.stream().map(p ->
                Instance.builder()
                        .serviceName(p.getServiceName())
                        .port(p.getPort())
                        .ip(p.getIp())
                        .clusterName(p.getClusterName())
                        .enable(p.isEnabled())
                        .healthy(p.isHealthy())
                        .metaData(p.getMetadata())
                        .weight((byte) p.getWeight()).build()).filter(p ->
        {
            for (ISimpleFilter filter : filters)
            {
                if (filter.filter(p))
                {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

}
