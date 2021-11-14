package com.cell.metrics.prometheus.sd.extension.sd;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.core.filters.ISimpleFilter;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
import com.cell.metrics.prometheus.sd.extension.model.ServiceInstanceHealth;
import com.cell.node.discovery.model.Instance;
import com.cell.node.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.node.discovery.nacos.util.DiscoveryUtils;
import com.cell.node.discovery.service.INodeDiscovery;
import com.cell.sdk.log.LOG;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-22 21:20
 */
public class RegistrationService extends AbstractInitOnce implements IPrometheusServiceDiscovery
{
    private static final String[] NO_SERVICE_TAGS = new String[0];

    private INodeDiscovery nodeDiscovery;
    private volatile boolean onChange = false;
    private Map<String, List<Instance>> instances = new HashMap<>();
    private final Map<String, List<Instance>> delta = new HashMap<>();
    private final List<Instance> down = new ArrayList<>();

    private static final ISimpleFilter<Instance> FILTER = instance -> instance.isHealthy();
    private static final ISimpleFilter<Instance> HttpFilter = instance ->
    {
        Map<String, String> metaData = instance.getMetaData();
        if (metaData == null)
        {
            return false;
        }
        return (ServerMetaData.fromMetaData(metaData).getExtraInfo().getType() & ProtocolConstants.TYPE_HTTP) >= ProtocolConstants.TYPE_HTTP;
    };
    // TODO
    private String cluster;
    private Semaphore semaphore;
    private Long lastUpdateTimestamp;
    private static final long ONE_MIN = Duration.ofMinutes(1).toMillis();

    // TODO ,抽象到一个helper中
    private boolean tryAcquire()
    {
        boolean b = this.semaphore.tryAcquire();
        if (!b)
        {
            return false;
        }
        if (this.lastUpdateTimestamp == null || System.currentTimeMillis() - this.lastUpdateTimestamp > ONE_MIN)// 这里设置会导致,可能只有下一次才能触发
        {
            this.lastUpdateTimestamp = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    private void release()
    {
        this.semaphore.release();
    }

    public Mono<ChangeItem<Map<String, String[]>>> getServiceNames(long waitMillis, Long index)
    {
        return returnDeferred(waitMillis, index, () ->
        {
            this.transferIfNeed();

            Set<String> set = new HashSet<>();
            for (String service : this.instances.keySet())
            {
                if (CollectionUtils.isEmpty(this.instances.get(service))) continue;
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

    private void transferIfNeed()
    {
        if (this.onChange)
        {
            synchronized (this.delta)
            {
                for (String service : delta.keySet())
                {
                    List<Instance> instances = this.delta.get(service);
                    List<Instance> origin = this.instances.get(service);
                    if (CollectionUtils.isNotEmpty(origin))
                    {
                        List<Instance> down = instances.stream().filter(p -> origin.contains(p)).collect(Collectors.toList());
                        this.down.addAll(down);
                    }
                    int code = service.hashCode();
                    LOG.info(Module.SD_PROMETHEUS, "添加service,{}", service);
                    this.instances.put(service, instances);
                }
                this.onChange = false;
            }
        }
    }

    public Mono<ChangeItem<List<Map<String, Object>>>> getService(String serviceName, long waitMillis, Long index)
    {
        return returnDeferred(waitMillis, index, () ->
        {
            this.transferIfNeed();

            List<Instance> instances = this.instances.get(serviceName);
            List<Map<String, Object>> list = new ArrayList<>();

            if (instances == null)
            {
                return Collections.emptyList();
            } else
            {
                Set<Instance> instSet = new HashSet<>(instances);
                for (Instance instance : instSet)
                {
                    Map<String, Object> ipObj = new HashMap<>();

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

            List<Instance> instances = this.instances.get(serviceName);

            if (instances == null)
            {
                return Collections.emptyList();
            }

            Set<Instance> instSet = new HashSet<>(instances);
            return instSet.stream().map(instance ->
            {
                ServerMetaData serverMetaData = ServerMetaData.fromMetaData(instance.getMetaData());

                ServiceInstanceHealth.Node node = ServiceInstanceHealth.Node.builder()
                        .address(serverMetaData.getExtraInfo().getPublicNetwork().getAddress())
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

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.semaphore = new Semaphore(1);
        this.nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
//        List<String> allServices = this.nodeDiscovery.getAllServices();
//        for (String allService : allServices)
//        {
//            List<Instance> serviceAllInstance = this.nodeDiscovery.getServiceAllInstance(allService, FILTER, HttpFilter);
//            LOG.info(Module.SD_PROMETHEUS, "添加service,{}", allService);
//            this.instances.put(allService, serviceAllInstance);
//        }
        ((NacosNodeDiscoveryImpl) this.nodeDiscovery).registerListen(new Listener());

        this.schedualFlush();
    }

    private void schedualFlush()
    {
        Flux.interval(Duration.ofMinutes(5)).map(v ->
        {
            if (!this.tryAcquire())
            {
                return new ArrayList<String>();
            }
            LOG.info("开始定时请求nacos,刷新服务列表");
            List<String> allServices = nodeDiscovery.getAllServices();
            return allServices;
        }).subscribe(allServices ->
        {
            try
            {
                if (allServices == null || allServices.size() == 0)
                {
                    return;
                }
                synchronized (this.delta)
                {
                    // FIXME ,这块逻辑有问题
                    boolean added = false;
                    for (String s : allServices)
                    {
                        List<Instance> instances = this.nodeDiscovery.getServiceAllInstance(s, this.FILTER, this.HttpFilter);
                        if (CollectionUtils.isNotEmpty(instances))
                        {
                            added = true;
                            this.delta.put(instances.get(0).getServiceName(), instances);
                        }
                    }
                    if (added)
                    {
                        this.onChange = true;
                    }
                }
            } finally
            {
                this.release();
            }
        });
    }

    class Listener extends Subscriber<InstancesChangeEvent>
    {
        @Override
        public void onEvent(InstancesChangeEvent event)
        {
            LOG.info(Module.SD_PROMETHEUS, "收到event:{},hosts:{}", event);
            // FIXME , 处理nacos 的cluster
            String clusters = event.getClusters();
            RegistrationService.this.lastUpdateTimestamp = System.currentTimeMillis();
            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts().stream().filter(e ->
                    e.getClusterName().equalsIgnoreCase(RegistrationService.this.cluster)).collect(Collectors.toList());
            synchronized (RegistrationService.this.delta)
            {
                RegistrationService.this.delta.put(event.getServiceName(), DiscoveryUtils.convNaocsInstance2CellInstance(hosts));
                RegistrationService.this.onChange = true;
            }
        }

        @Override
        public Class<? extends Event> subscribeType()
        {
            return InstancesChangeEvent.class;
        }
    }
}
