package com.cell.discovery.nacos.discovery.abs;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.SmartSubscriber;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.bee.event.simple.ISimpleEventHook;
import com.cell.bee.event.simple.SimpleJobCenter;
import com.cell.bee.event.simple.SimpleJobCenterFactory;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.loadbalance.utils.LBUtils;
import com.cell.constants.DebugConstants;
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.IInstanceEventListener;
import com.cell.discovery.nacos.discovery.IInstanceOnChange;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.events.IEvent;
import com.cell.exceptions.ProgramaException;
import com.cell.executor.IChainExecutor;
import com.cell.filters.ISimpleFilter;
import com.cell.grpc.common.config.AbstractInitOnce;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Couple;
import com.cell.models.Module;
import com.cell.models.Quadruple;
import com.cell.rpc.grpc.client.framework.util.DiscoveryUtils;
import com.cell.transport.model.ServerMetaData;
import com.cell.utils.StringUtils;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 10:40
 */
@Data
public class ServiceDiscoverySchedual extends AbstractInitOnce
{
    public static final String nodeDiscoveryStr = "nodeDiscovery";
    private static final ServiceDiscoverySchedual instance = new ServiceDiscoverySchedual();

    private INacosNodeDiscovery nodeDiscovery;
    private final SimpleJobCenter simpleJobCenter = SimpleJobCenterFactory.NewSimpleJobCenter();
    private ISimpleFilter<Instance> filter = instance -> instance.isHealthy();
    private String cluster;


    public void addListener(IInstanceEventListener listener)
    {
        simpleJobCenter.registerEventHook(new InternalHook(listener));
    }

    class InternalHook implements ISimpleEventHook
    {
        final IInstanceEventListener listener;

        InternalHook(IInstanceEventListener listener) {this.listener = listener;}

        @Override
        public Mono<Void> execute(IEvent event, IChainExecutor<IEvent> executor)
        {
            this.listener.onListen((IInstanceEventListener.InstanceEventWrapper) event);
            return executor.execute(event);
        }
    }

    public static ServiceDiscoverySchedual getInstance()
    {
        return instance;
    }

    private Map<String, Set<ServerCmdMetaInfo>> lastUpdateServerMetas = new HashMap<>(1);
    protected Snap delta;


    @Override
    protected void onInit(InitCTX ctx)
    {
        Map<String, Object> data = ctx.getData();
        String cluster = (String) data.get("cluster");
        if (StringUtils.isEmpty(cluster))
        {
            throw new ProgramaException("ad");
        }
        INacosNodeDiscovery n = (INacosNodeDiscovery) data.get(nodeDiscoveryStr);
        if (n == null)
        {
            throw new ProgramaException("sad");
        }
        this.cluster = cluster;
        this.nodeDiscovery = n;
        this.schedualRefresh();
    }

    private void schedualRefresh()
    {
        Flux.interval(Duration.ofMinutes(1)).map(v ->
        {
            Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster, this.filter);
            return serverInstanceList;
        }).subscribe(serverInstanceList ->
        {
            if (serverInstanceList.size() == 0)
            {
                return;
            }
            this.simpleJobCenter.addJob(new IInstanceEventListener.InstanceEventWrapper(serverInstanceList));
        });
    }

    private class InstanceHooker extends SmartSubscriber
    {
        @Override
        public List<Class<? extends Event>> subscribeTypes()
        {
            return Arrays.asList(InstancesChangeEvent.class);
        }

        @Override
        public void onEvent(Event eee)
        {
            LOG.info(Module.HTTP_GATEWAY, "收到event:{}", eee);
            if (!(eee instanceof InstancesChangeEvent))
            {
                return;
            }
            InstancesChangeEvent event = (InstancesChangeEvent) eee;
            // FIXME , 处理nacos 的cluster
            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts().stream().filter(e ->
                    e.getClusterName().equalsIgnoreCase(ServiceDiscoverySchedual.this.cluster)).collect(Collectors.toList());
            Map<String, List<Instance>> serverInstanceList = new HashMap<>();
            serverInstanceList.put(event.getServiceName(), DiscoveryUtils.convNaocsInstance2CellInstance(hosts));
        }
    }
}
