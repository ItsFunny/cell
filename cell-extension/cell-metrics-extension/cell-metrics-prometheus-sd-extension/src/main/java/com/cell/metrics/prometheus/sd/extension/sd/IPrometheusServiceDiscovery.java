package com.cell.metrics.prometheus.sd.extension.sd;

import com.cell.base.common.context.IInitOnce;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
import com.cell.metrics.prometheus.sd.extension.model.ServiceInstanceHealth;
import com.cell.node.discovery.nacos.discovery.IServiceDiscovery;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-10 21:20
 */
public interface IPrometheusServiceDiscovery extends IInitOnce
{
    Mono<ChangeItem<List<ServiceInstanceHealth>>> getServiceHealth(String serviceName, long waitMillis, Long index);

    Mono<ChangeItem<List<Map<String, Object>>>> getService(String serviceName, long waitMillis, Long index);

    Mono<ChangeItem<Map<String, String[]>>> getServiceNames(long waitMillis, Long index);
}
