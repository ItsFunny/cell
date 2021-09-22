package com.cell.sd;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.discovery.NacosNodeDiscoveryImpl;
import com.cell.log.LOG;
import com.cell.model.ChangeItem;
import com.cell.model.Instance;
import com.cell.models.Module;
import com.cell.service.INodeDiscovery;
import com.cell.util.DiscoveryUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
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
public class RegistrationService extends AbstractInitOnce
{
    private static final String[] NO_SERVICE_TAGS = new String[0];

    private INodeDiscovery nodeDiscovery;
    private volatile boolean onChange = false;
    private Map<String, List<Instance>> instances = new HashMap<>();
    private final Map<String, List<Instance>> delta = new HashMap<>();
    private final List<Instance> down = new ArrayList<>();


    public Mono<ChangeItem<Map<String, String[]>>> getServiceNames(long waitMillis, Long index)
    {
        return returnDeferred(waitMillis, index, () ->
        {
            if (this.onChange)
            {
                synchronized (this.delta)
                {
                    for (String service : delta.keySet())
                    {
                        List<Instance> instances = this.delta.get(service);
                        List<Instance> origin = this.instances.get(service);
                        List<Instance> down = instances.stream().filter(p -> origin.contains(p)).collect(Collectors.toList());

                        this.down.addAll(down);
                        this.instances.put(service, instances);
                    }
                }
            }
            Set<String> set = new HashSet<String>();
            for (String service : this.instances.keySet())
            {
                set.add(service);
            }
            Map<String, String[]> result = new HashMap<String, String[]>();
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
                    Map<String, Object> ipObj = new HashMap<String, Object>();

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

    private <T> Mono<ChangeItem<T>> returnDeferred(long waitMillis, Long index, Supplier<T> fn)
    {
        return Mono.just(new ChangeItem<>(fn.get(), new Date().getTime()));
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
        List<String> allServices = this.nodeDiscovery.getAllServices();

        for (String allService : allServices)
        {
            List<Instance> serviceAllInstance = this.nodeDiscovery.getServiceAllInstance(allService);
            this.instances.put(allService, serviceAllInstance);
        }
        ((NacosNodeDiscoveryImpl) this.nodeDiscovery).registerListen(new Listener());

        this.schedualFlush();
    }

    private void schedualFlush()
    {
        Flux.interval(Duration.ofMinutes(5)).map(v ->
        {
            List<String> allServices = nodeDiscovery.getAllServices();
            return allServices;
        }).subscribe(allServices ->
        {
            if (allServices.size() == 0)
            {
                return;
            }
            synchronized (this.delta)
            {
                for (String s : allServices)
                {
                    List<Instance> instances = this.nodeDiscovery.getServiceAllInstance(s);
                    this.delta.put(instances.get(0).getClusterName(), instances);
                }
                this.onChange = true;
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

            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts();
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