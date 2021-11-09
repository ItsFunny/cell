package com.cell.node.discovery.nacos.discovery.abs;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.SmartSubscriber;
import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.base.common.events.IEvent;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.filters.ISimpleFilter;
import com.cell.bee.event.simple.ISimpleEventHook;
import com.cell.bee.event.simple.SimpleJobCenter;
import com.cell.bee.event.simple.SimpleJobCenterFactory;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.node.discovery.model.Instance;
import com.cell.node.discovery.nacos.discovery.IInstanceEventListener;
import com.cell.node.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.node.discovery.nacos.util.DiscoveryUtils;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.sdk.log.LOG;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Semaphore;
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

    private Semaphore semaphore;

    private ServiceDiscoverySchedual()
    {
        this.semaphore = new Semaphore(1);
    }


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

    private Long lastUpdateTimestamp;
    private static final long ONE_MIN = Duration.ofMinutes(1).toMillis();

    private boolean tryAcquire()
    {
        boolean b = this.semaphore.tryAcquire();
        if (!b)
        {
            return false;
        }
        if (this.lastUpdateTimestamp == null || System.currentTimeMillis() - this.lastUpdateTimestamp > ONE_MIN)
        {
            return true;
        }
        return false;
    }


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
            this.tryAcquire();
            Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster, this.filter);
            return serverInstanceList;
        }).subscribe(serverInstanceList ->
        {
            try
            {
                if (serverInstanceList.size() == 0)
                {
                    return;
                }
                this.simpleJobCenter.addJob(new IInstanceEventListener.InstanceEventWrapper(serverInstanceList));
            } finally
            {
                this.release();
            }
        });
    }

    private void release()
    {
        this.lastUpdateTimestamp = System.currentTimeMillis();
        this.semaphore.release();
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
