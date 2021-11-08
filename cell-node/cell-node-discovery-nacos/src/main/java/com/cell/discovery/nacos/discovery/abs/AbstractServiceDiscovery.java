package com.cell.discovery.nacos.discovery.abs;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.SmartSubscriber;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.annotations.AutoPlugin;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.loadbalance.utils.LBUtils;
import com.cell.grpc.common.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.IInstanceOnChange;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.discovery.nacos.discovery.IServiceDiscovery;
import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.exceptions.ConfigException;
import com.cell.filters.ISimpleFilter;
import com.cell.lb.ILoadBalancer;
import com.cell.lb.ILoadBalancerStrategy;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Couple;
import com.cell.models.Module;
import com.cell.models.Quadruple;
import com.cell.resolver.IKeyResolver;
import com.cell.rpc.grpc.client.framework.util.DiscoveryUtils;
import com.cell.transport.model.ServerMetaData;
import lombok.Data;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 21:03
 */
public abstract class AbstractServiceDiscovery<K1, K2> extends AbstractInitOnce implements IServiceDiscovery
{
    public static final String callBackStr = "callback";
    protected String cluster;

    protected INacosNodeDiscovery nodeDiscovery;

    protected IKeyResolver<K1, K2> resolver;
    protected ILoadBalancer loadBalancer;
    protected IInstanceOnChange callBack;

    protected Map<String, Set<ServerCmdMetaInfo>> serverMetas = new HashMap<>();

    protected Snap delta;
    protected volatile boolean onChange = false;
    private AtomicLong changeSeq = new AtomicLong();
    // FOR LOCK LESS
    private Map<String, Set<ServerCmdMetaInfo>> lastUpdateServerMetas = new HashMap<>(1);

    private ISimpleFilter<Instance> filter = instance -> instance.isHealthy();

    private ServiceDiscoverySchedual serviceDiscoverySchedual;

    public AbstractServiceDiscovery()
    {
        serviceDiscoverySchedual = ServiceDiscoverySchedual.getInstance();
    }


    @AutoPlugin
    private void setLoadBalancerStrategy(ILoadBalancerStrategy strategy)
    {
        this.loadBalancer = new DefaultClientLoadBalancer(strategy);
    }

    @Override
    public Map<String, Set<ServerCmdMetaInfo>> getServerMetas()
    {
        return this.serverMetas;
    }

    public ServerCmdMetaInfo choseServer(String method, String protocol)
    {
        this.transferIfNeed();
        return this.loadBalancer.choseServer(this.doGetServerByProtocol(method, protocol), method, protocol);
    }

    protected abstract Collection<ServerCmdMetaInfo> doGetServerByProtocol(String method, String protocol);

    private Map<String, Set<ServerCmdMetaInfo>> deepCopy()
    {
        final Map<String, Set<ServerCmdMetaInfo>> lastUpdateServerMetas = this.lastUpdateServerMetas;
        Map<String, Set<ServerCmdMetaInfo>> ret = new HashMap<>();
        Set<String> strings = lastUpdateServerMetas.keySet();
        for (String string : strings)
        {
            Set<ServerCmdMetaInfo> serverCmdMetaInfos = lastUpdateServerMetas.get(string);
            ret.put(string, new HashSet<>(serverCmdMetaInfos));
        }
        return ret;
    }

    private void transferIfNeed()
    {
        if (!this.onChange) return;
        Map<String, List<Set<ServerCmdMetaInfo>>> compareChanges = new HashMap<>();
        final List<ServerCmdMetaInfo> dels = new ArrayList<>();
        synchronized (this)
        {
            if (!this.onChange) return;
            try
            {
                this.serverMetas = this.deepCopy();
                LOG.info(Module.DISCOVERY, "更新serverMetas:{}", this.serverMetas);
            } finally
            {
                // TODO EXCEPTION CATCH
                this.callBack.onChange(this.delta);
                this.delta = null;
                this.onChange = false;
            }
        }
        LOG.info(Module.HTTP_GATEWAY, "删除的router:{},变更的信息:{}", dels, compareChanges);
    }


    /*
        1. 计算新增的protocol:
            1.1 可能是有新的服务启动,注册到了nacos中,
        2. 计算减去的protocol:
            2.1 可能是服务宕机,
     */
    private Set<String> protocols = new HashSet<>(1);

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

        Couple<Map<String, Set<ServerCmdMetaInfo>>, Map<String, InstanceWrapper>> couple = this.convCellInstanceToGateMeta(serverInstanceList);
        Map<String, InstanceWrapper> v2 = couple.getV2();
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

    private Couple<Map<String, Set<ServerCmdMetaInfo>>, Map<String, InstanceWrapper>> convCellInstanceToGateMeta(Map<String, List<Instance>> m)
    {
        Set<String> keys = m.keySet();
        Map<String, Set<ServerCmdMetaInfo>> metas = new HashMap<>();
        Map<String, InstanceWrapper> instanceWrapperMap = new HashMap<>();
        keys.stream().forEach(k ->
        {
            List<Instance> instances = m.get(k);
            instances.stream().forEach(inst ->
                    {
                        InstanceWrapper wrapper = new InstanceWrapper();
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

    private String createKeyByInstance(Instance instance)
    {
        return instance.getIp() + "" + instance.getPort();
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.resolver = newKeyResolver();
        this.nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();

        this.beforeInit(ctx);
        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster, this.filter);
        Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> qudr = this.compare(serverInstanceList);

        this.serverMetas = qudr.getV2();
        this.protocols = qudr.getV3();

        if (this.callBack == null)
        {
            IInstanceOnChange c = (IInstanceOnChange) ctx.getData().get(callBackStr);
            if (c == null)
            {
                throw new ConfigException("asd");
            }
            this.setCallBack(c);
        }
        this.lastUpdateServerMetas = this.serverMetas;
        this.callBack.onChange(qudr.getV1());
//        nodeDiscovery.registerListen(new InstanceHooker());
        this.afterInit(ctx);

        ctx.getData().put(ServiceDiscoverySchedual.nodeDiscoveryStr, this.nodeDiscovery);
        ctx.getData().put("cluster", this.cluster);
        this.serviceDiscoverySchedual.initOnce(ctx);
        this.serviceDiscoverySchedual.addListener((w) ->
        {
            Map<String, List<Instance>> ss = w.getInstances();
            if (ss.size() == 0)
            {
                return;
            }
            Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> compare = this.compare(ss);
            this.updateIfNeeded(compare);
        });
    }

    protected abstract void beforeInit(InitCTX ctx);

    protected abstract void afterInit(InitCTX ctx);

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
                    e.getClusterName().equalsIgnoreCase(AbstractServiceDiscovery.this.cluster)).collect(Collectors.toList());
            Map<String, List<Instance>> serverInstanceList = new HashMap<>();
            serverInstanceList.put(event.getServiceName(), DiscoveryUtils.convNaocsInstance2CellInstance(hosts));
            Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> compare = AbstractServiceDiscovery.this.compare(serverInstanceList);
            AbstractServiceDiscovery.this.updateIfNeeded(compare);
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
            LOG.info(Module.DISCOVERY, "更新lastUpdateServerMetas:{},{}", this.lastUpdateServerMetas, this.getClass().getName());
            AbstractServiceDiscovery.this.onChange = true;
        }
    }

    public void setCallBack(IInstanceOnChange callBack)
    {
        this.callBack = callBack;
    }

//    private void schedualRefresh()
//    {
//        Flux.interval(Duration.ofMinutes(1)).map(v ->
//        {
//            Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster, this.filter);
//            return serverInstanceList;
//        }).subscribe(serverInstanceList ->
//        {
//            if (serverInstanceList.size() == 0)
//            {
//                return;
//            }
//            Quadruple<Snap, Map<String, Set<ServerCmdMetaInfo>>, Set<String>, Boolean> compare = this.compare(serverInstanceList);
//            this.updateIfNeeded(compare);
//        });
//    }

    @Data
    public static class InstanceWrapper
    {
        private Instance instance;
    }


//    private void filterWith(Couple<Map<String, Set<ServerCmdMetaInfo>>, Map<String, InstanceWrapper>> couple)
//    {
//        Set<String> keys = this.lastServerMetas.keySet();
//        Set<ServerCmdMetaInfo> newCmds = new HashSet<>();
//
//        /*
//            需要校验的字段:
//            1. 新增的instance
//            2. 宕机的instance
//            3. 新增的 protocol: 可能原先对应的instance
//            4. 宕机的 protocol: 可能只是部分protocol 宕机,所以并不能释放全部的stub
//         */
//
//        for (String key : keys)
//        {
//            Set<ServerCmdMetaInfo> update = conv.get(key);
//            Set<ServerCmdMetaInfo> origin = this.lastServerMetas.get(key);
//
//
//            for (ServerCmdMetaInfo serverCmdMetaInfo : origin)
//            {
//
//            }
//        }
//        return null;
//    }

    protected abstract IKeyResolver<K1, K2> newKeyResolver();

    protected abstract String resolve(IKeyResolver<K1, K2> resolver, ServerMetaData.ServerMetaCmd cmd);

    protected abstract byte filterType();


    class DefaultClientLoadBalancer implements ILoadBalancer
    {
        private ILoadBalancerStrategy strategy;

        public DefaultClientLoadBalancer(ILoadBalancerStrategy strategy)
        {
            this.strategy = strategy;
        }

        @Override
        public ServerCmdMetaInfo choseServer(Collection<ServerCmdMetaInfo> servers, String method, String protocol)
        {
            return this.strategy.choseServer(servers, protocol);
        }
    }

    public String getCluster()
    {
        return cluster;
    }

    @Override
    public void setCluster(String cluster)
    {
        this.cluster = cluster;
    }
}