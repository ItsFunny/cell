package com.cell.metrics.prometheus.sd.extension.sd;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.context.InitCTX;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
import com.cell.metrics.prometheus.sd.extension.model.ServiceInstanceHealth;
import com.cell.node.discovery.model.Instance;
import com.cell.node.discovery.nacos.discovery.abs.AbstractServiceDiscovery;
import com.cell.node.discovery.nacos.discovery.abs.InstanceWrapper;
import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultPureStringKeyResolver;
import com.cell.sdk.log.LOG;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 09:29
 */
public class PrometheusServiceDiscovery extends AbstractServiceDiscovery<String, String> implements IPrometheusServiceDiscovery
{

    public PrometheusServiceDiscovery()
    {
        this.setCallBack((snap ->
        {
            LOG.info(Module.SD_PROMETHEUS, "收到了服务变更消息");
        }));
    }

    @Override
    protected Collection<ServerCmdMetaInfo> doGetServerByProtocol(String method, String protocol)
    {
        throw new RuntimeException("not supposed");
    }

    @Override
    protected void beforeInit(InitCTX ctx)
    {

    }

    @Override
    protected void afterInit(InitCTX ctx)
    {

    }

    @Override
    protected IKeyResolver<String, String> newKeyResolver()
    {
        return new DefaultPureStringKeyResolver();
    }

    @Override
    protected String resolve(IKeyResolver<String, String> resolver, ServerMetaData.ServerMetaCmd cmd)
    {
        return this.resolver.resolve(cmd.getProtocol());
    }


    @Override
    protected byte filterType()
    {
        return ProtocolConstants.TYPE_HTTP;
    }


    private static final String[] NO_SERVICE_TAGS = new String[0];


    public Mono<ChangeItem<Map<String, String[]>>> getServiceNames(long waitMillis, Long index)
    {
        return returnDeferred(waitMillis, index, () ->
        {
            this.transferIfNeed();
            Set<String> set = new HashSet<>();
            Map<String, List<InstanceWrapper>> instances = this.instances;
            if (instances == null || instances.size() == 0)
            {
                return new HashMap<>();
            }
            for (String service : instances.keySet())
            {
                if (CollectionUtils.isEmpty(instances.get(service))) continue;
                set.add(service);
            }
            Map<String, String[]> result = new HashMap<>();
            for (String item : set)
            {
                result.put(item, NO_SERVICE_TAGS);
            }
            return result;
        });
    }

    public Mono<ChangeItem<List<Map<String, Object>>>> getService(String serviceName, long waitMillis, Long index)
    {
        return returnDeferred(waitMillis, index, () ->
        {
            this.transferIfNeed();

            final Map<String, List<InstanceWrapper>> currentInstances = this.instances;
            List<InstanceWrapper> instances = currentInstances.get(serviceName);
            List<Map<String, Object>> list = new ArrayList<>();

            if (instances == null)
            {
                return Collections.emptyList();
            } else
            {
                Set<InstanceWrapper> instSet = new HashSet<>(instances);
                for (InstanceWrapper instanceWrapper : instSet)
                {
                    Map<String, Object> ipObj = new HashMap<>();
                    Instance instance = instanceWrapper.getInstance();
                    ipObj.put("Address", instance.getIp());
                    ipObj.put("Node", instance.getServiceName());
                    ipObj.put("ServiceAddress", instance.getIp());
                    ipObj.put("ServiceName", instance.getServiceName());
                    ipObj.put("ServiceID", instance.getIp() + ":" + instance.getPort());
                    ipObj.put("ServicePort", instance.getPort());
                    ipObj.put("NodeMeta", Collections.emptyMap());
                    Map<String, String> metaJo = new HashMap<String, String>();
                    metaJo.put("management.port", "" + instance.getPort());
                    ipObj.put("ServiceMeta", metaJo);
                    ipObj.put("ServiceTags", Collections.emptyList());
                    list.add(ipObj);
                }
                return list;
            }
        });
    }


    public Mono<ChangeItem<List<ServiceInstanceHealth>>> getServiceHealth(String serviceName, long waitMillis, Long index)
    {
        return returnDeferred(waitMillis, index, () ->
        {
            this.transferIfNeed();
            Map<String, List<InstanceWrapper>> currentInstances = this.instances;
            List<InstanceWrapper> instances = currentInstances.get(serviceName);

            if (instances == null)
            {
                return Collections.emptyList();
            }

            Set<InstanceWrapper> instSet = new HashSet<>(instances);
            return instSet.stream().map(instanceWrapper ->
            {
                Instance instance = instanceWrapper.getInstance();
                ServerMetaData serverMetaData = ServerMetaData.fromMetaData(instance.getMetaData());

                ServiceInstanceHealth.Node node = ServiceInstanceHealth.Node.builder()
                        .address(serverMetaData.getExtraInfo().getDomain())
                        .id(instance.getServiceName())
                        .dataCenter("ad")
                        .build();
                ServiceInstanceHealth.Service service = ServiceInstanceHealth.Service.builder()
                        .service(instance.getServiceName())
                        .id(instance.getServiceName() + "_" + instance.getPort())
                        .port(instance.getPort())
                        .build();
                return ServiceInstanceHealth.builder().node(node).service(service).build();
            }).collect(Collectors.toList());
        });
    }

    private <T> Mono<ChangeItem<T>> returnDeferred(long waitMillis, Long index, Supplier<T> fn)
    {
        return Mono.just(new ChangeItem<>(fn.get(), new Date().getTime()));
    }
}
