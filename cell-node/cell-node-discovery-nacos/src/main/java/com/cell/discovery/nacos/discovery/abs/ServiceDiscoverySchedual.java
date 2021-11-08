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
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.IInstanceOnChange;
import com.cell.events.IEvent;
import com.cell.executor.IChainExecutor;
import com.cell.grpc.common.config.AbstractInitOnce;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Couple;
import com.cell.models.Module;
import com.cell.models.Quadruple;
import com.cell.rpc.grpc.client.framework.util.DiscoveryUtils;
import com.cell.transport.model.ServerMetaData;
import lombok.Data;
import reactor.core.publisher.Mono;

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
    public static final String nodeDiscovery = "nodeDiscovery";
    private static final ServiceDiscoverySchedual instance = new ServiceDiscoverySchedual();

    private final SimpleJobCenter simpleJobCenter = SimpleJobCenterFactory.NewSimpleJobCenter();
    private String cluster;


    public void addHook(IInstanceOnChange iInstanceOnChange)
    {
        simpleJobCenter.registerEventHook(new InternalHook(iInstanceOnChange));
    }

    class InternalHook implements ISimpleEventHook
    {
        final IInstanceOnChange change;

        InternalHook(IInstanceOnChange change) {this.change = change;}

        @Override
        public Mono<Void> execute(IEvent event, IChainExecutor<IEvent> executor)
        {
            this.change.onChange((Snap) event);
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
            Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> compare = ServiceDiscoverySchedual.this.compare(serverInstanceList);
            ServiceDiscoverySchedual.this.updateIfNeeded(compare);
        }
    }
    private void updateIfNeeded(Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> compare)
    {
        if (!compare.getV4())
        {
            return;
        }
        synchronized (AbstractServiceDiscovery.this)
        {
            AbstractServiceDiscovery.this.delta = compare.getV1();
            AbstractServiceDiscovery.this.protocols = compare.getV3();
            /*
                存在间隔中缓存丢失的问题
                这里不更新currentServerMetas, currentServerMetas,只会在请求到来的时候进行更新
             */
            AbstractServiceDiscovery.this.lastUpdateServerMetas = compare.getV2();
            LOG.info(Module.DISCOVERY, "更新lastUpdateServerMetas:{}", this.lastUpdateServerMetas);
            AbstractServiceDiscovery.this.onChange = true;
        }
    }

    private Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> compare(Map<String, List<Instance>> serverInstanceList)
    {
        long l = AbstractServiceDiscovery.this.changeSeq.get();
        boolean b = AbstractServiceDiscovery.this.changeSeq.compareAndSet(l, l + 1);
        if (!b)
        {
            return new Quadruple<>(null, null, null, false);
        }
        Snap ret = new Snap();
        Map<String, Set<ServerCmdMetaInfo>> newProtocols = new HashMap<>();
        Map<String, Set<ServerCmdMetaInfo>> deltaAddProtocols = new HashMap<>();

        Map<String, Set<ServerCmdMetaInfo>> downProtocols = new HashMap<>();
        Map<String, Set<ServerCmdMetaInfo>> deltaDownProtocols = new HashMap<>();

        final Set<String> originAllProtocols = new HashSet<>(this.protocols);
        final Map<String, Set<ServerCmdMetaInfo>> originAllMetas = new HashMap<>(this.lastUpdateServerMetas);
        final Set<String> newAllProtocols = new HashSet<>();
        ret.deltaAddProtocols = deltaAddProtocols;
        ret.deltaDownProtocols = deltaDownProtocols;
        ret.newProtocols = newProtocols;
        ret.downProtocols = downProtocols;

        Couple<Map<String, Set<ServerCmdMetaInfo>>, Map<String, AbstractServiceDiscovery.InstanceWrapper>> couple = this.convCellInstanceToGateMeta(serverInstanceList);
        Map<String, AbstractServiceDiscovery.InstanceWrapper> v2 = couple.getV2();
        ret.instances = v2;

        Map<String, Set<ServerCmdMetaInfo>> protoMetas = couple.getV1();
        for (String s : protoMetas.keySet())
        {
            if (CollectionUtils.isEmpty(protoMetas.get(s)))
            {
                throw new RuntimeException("asd");
            }
        }
        for (String protocol : protoMetas.keySet())
        {
            newAllProtocols.add(protocol);

            Set<ServerCmdMetaInfo> cmds = protoMetas.get(protocol);
            if (!originAllProtocols.contains(protocol))
            {
                newProtocols.put(protocol, cmds);
                continue;
            }
            originAllProtocols.remove(protocol);

            Set<ServerCmdMetaInfo> origins = originAllMetas.get(protocol);
            // 1. 增量新增 : 既protocol 原先存在,有新的instance up
            // 2. 增量宕机: 既protocol某几个instance 宕机,
            for (ServerCmdMetaInfo cmd : cmds)
            {
                if (!CollectionUtils.isEmpty(origins))
                {
                    // 如果原先存在,则直接跳过
                    if (origins.contains(cmd))
                    {
                        origins.remove(cmd);
                        continue;
                    }
                }
                // 表明是 增量新增 protocol
                Set<ServerCmdMetaInfo> serverCmdMetaInfos = deltaAddProtocols.get(protocol);
                if (CollectionUtils.isEmpty(serverCmdMetaInfos))
                {
                    serverCmdMetaInfos = new HashSet<>();
                    deltaAddProtocols.put(protocol, serverCmdMetaInfos);
                }
                serverCmdMetaInfos.add(cmd);
            }

            if (CollectionUtils.isNotEmpty(origins))
            {
                // 剩下的则为增量 宕机的
                deltaDownProtocols.put(protocol, origins);
            }
        }

        // 剩下的则为全部宕机
        for (String down : originAllProtocols)
        {
            Set<ServerCmdMetaInfo> serverCmdMetaInfos = originAllMetas.get(down);
            if (CollectionUtils.isNotEmpty(serverCmdMetaInfos))
            {
                downProtocols.put(down, serverCmdMetaInfos);
            }
        }
        return new Quadruple(ret, protoMetas, newAllProtocols, (newProtocols.size() > 0 || deltaAddProtocols.size() > 0 || downProtocols.size() > 0 || deltaDownProtocols.size() > 0));
    }

    private Couple<Map<String, Set<ServerCmdMetaInfo>>, Map<String, AbstractServiceDiscovery.InstanceWrapper>> convCellInstanceToGateMeta(Map<String, List<Instance>> m)
    {
        Set<String> keys = m.keySet();
        Map<String, Set<ServerCmdMetaInfo>> metas = new HashMap<>();
        Map<String, AbstractServiceDiscovery.InstanceWrapper> instanceWrapperMap = new HashMap<>();
        keys.stream().forEach(k ->
        {
            List<Instance> instances = m.get(k);
            instances.stream().forEach(inst ->
                    {
                        AbstractServiceDiscovery.InstanceWrapper wrapper = new AbstractServiceDiscovery.InstanceWrapper();
                        wrapper.instance = inst;
                        instanceWrapperMap.put(createKeyByInstance(inst), wrapper);

                        ServerMetaInfo info = LBUtils.fromInstance(inst);
                        ServerMetaData metaData = info.getMetaData();
                        if (metaData.getExtraInfo() == null || (metaData.getExtraInfo().getType() & this.filterType()) < this.filterType())
                        {
                            return;
                        }

                        List<ServerMetaData.ServerMetaReactor> reactors = metaData.getReactors();
                        if (CollectionUtils.isEmpty(reactors)) return;
                        reactors.stream().forEach(r ->
                        {
                            List<ServerMetaData.ServerMetaCmd> cmds = r.getCmds();
                            if (CollectionUtils.isEmpty(cmds)) return;
                            cmds.stream().forEach(c ->
                            {
                                String key = this.resolve(this.resolver, c);
                                Set<ServerCmdMetaInfo> serverMetaInfos = metas.get(key);
                                if (CollectionUtils.isEmpty(serverMetaInfos))
                                {
                                    serverMetaInfos = new HashSet<>();
                                    metas.put(key, serverMetaInfos);
                                }
                                ServerCmdMetaInfo serverCmdMetaInfo = ServerCmdMetaInfo.fromServerMetaInfo(info, c.getProtocol(), c.getModule());
                                serverMetaInfos.add(serverCmdMetaInfo);
                            });
                        });
                    }
            );
        });
        return new Couple<>(metas, instanceWrapperMap);
    }
}
