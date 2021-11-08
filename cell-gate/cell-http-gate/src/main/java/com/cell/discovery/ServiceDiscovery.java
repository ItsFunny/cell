//package com.cell.discovery;
//
//import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
//import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
//import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
//import com.alibaba.nacos.common.notify.Event;
//import com.alibaba.nacos.common.notify.listener.Subscriber;
//import com.alibaba.nacos.common.utils.CollectionUtils;
//import com.cell.annotations.AutoPlugin;
//import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
//import com.cell.bee.loadbalance.model.ServerMetaInfo;
//import com.cell.bee.loadbalance.utils.LBUtils;
//import com.cell.config.AbstractInitOnce;
//import com.cell.context.InitCTX;
//import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
//import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
//import com.cell.base.common.enums.EnumHttpRequestType;
//import com.cell.rpc.grpc.client.framework.util.DiscoveryUtils;
//import com.cell.lb.ILoadBalancer;
//import com.cell.lb.ILoadBalancerStrategy;
//import com.cell.log.LOG;
//import com.cell.model.Instance;
//import com.cell.models.Couple;
//import com.cell.base.common.models.Module;
//import com.cell.resolver.IKeyResolver;
//import com.cell.resolver.impl.DefaultStringKeyResolver;
//import com.cell.transport.model.ServerMetaData;
//import com.cell.utils.GatewayUtils;
//import lombok.Data;
//import reactor.core.publisher.Flux;
//
//import java.time.Duration;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-08 05:41
// */
//@Data
//public class ServiceDiscovery extends AbstractInitOnce
//{
//    private static ServiceDiscovery instance;
//
//    private INacosNodeDiscovery nodeDiscovery;
//
//    private IKeyResolver<DefaultStringKeyResolver.StringKeyResolver, String> resolver;
//
//    private ILoadBalancer loadBalancer;
//    private Map<String, List<ServerCmdMetaInfo>> serverMetas = new HashMap<>();
//    private final Map<String, List<Instance>> delta = new HashMap<>();
//    private volatile boolean onChange = false;
//    private String cluster;
//
//    @AutoPlugin
//    public void setInstance(ServiceDiscovery serviceDiscovery)
//    {
//        ServiceDiscovery.instance = serviceDiscovery;
//    }
//
//    @AutoPlugin
//    private void setLoadBalancerStrategy(ILoadBalancerStrategy strategy)
//    {
//        this.loadBalancer = new DefaultNacosLoadBalance(strategy);
//    }
//
//
//    public static ServiceDiscovery getInstance()
//    {
//        return instance;
//    }
//
//    private List<ServerCmdMetaInfo> getServerByUri(String method, String uri)
//    {
//        this.transferIfNeed();
//        // FIXME
//        return this.serverMetas.get(this.resolver.resolve(DefaultStringKeyResolver.StringKeyResolver.builder().method(method).uri(uri).build()));
//    }
//
//    public ServerCmdMetaInfo choseServer(String method, String uri)
//    {
//        return this.loadBalancer.choseServer(this.getServerByUri(method, uri), method, uri);
//    }
//
//    public synchronized Map<String, List<Instance>> getCurrentDelta()
//    {
//        return new HashMap<>(this.delta);
//    }
//
//    private void transferIfNeed()
//    {
//        if (!this.onChange) return;
//        // index:0
//        Map<String, List<List<ServerCmdMetaInfo>>> compareChanges = new HashMap<>();
//        final List<List<ServerCmdMetaInfo>> dels = new ArrayList<>();
//        final Set<RuleWp> ruleWps = new HashSet<>();
//        synchronized (this.delta)
//        {
//            if (!this.onChange) return;
//            Set<String> serviceNames = this.delta.keySet();
//            serviceNames.stream().forEach(n ->
//            {
//                List<Instance> instances = this.delta.get(n);
//                if (CollectionUtils.isEmpty(instances))
//                {
//                    dels.add(this.serverMetas.remove(n));
//                    this.delta.remove(n);
//                    this.serverMetas.remove(n);
//                    return;
//                }
//                Couple<Map<String, List<ServerCmdMetaInfo>>, Set<RuleWp>> convRet = this.conv();
//                Map<String, List<ServerCmdMetaInfo>> conv = convRet.getV1();
//                if (!CollectionUtils.isEmpty(convRet.getV2()))
//                {
//                    ruleWps.addAll(convRet.getV2());
//                }
//
//                Set<String> changes = conv.keySet();
//                changes.stream().forEach(name ->
//                {
//                    List<ServerCmdMetaInfo> metas = conv.get(name);
//                    List<ServerCmdMetaInfo> origin = this.serverMetas.get(name);
//                    this.serverMetas.put(name, metas);
//                    List<List<ServerCmdMetaInfo>> r = new ArrayList<>();
//                    r.add(origin);
//                    r.add(metas);
//                    compareChanges.put(name, r);
//                });
//            });
//            this.refreshUriRules(ruleWps);
//            this.delta.clear();
//            this.onChange = false;
//        }
//        LOG.info(Module.HTTP_GATEWAY, "删除的router:{},变更的信息:{}", dels, compareChanges);
//    }
//
//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//        this.resolver = new DefaultStringKeyResolver();
//        nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
//
//        this.registerSelf(ctx);
//
//        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster);
//        Couple<Map<String, List<ServerCmdMetaInfo>>, Set<RuleWp>> mapSetCouple = convCellInstanceToGateMeta(serverInstanceList);
//        this.serverMetas = mapSetCouple.getV1();
//        Set<RuleWp> ruleWpSet = mapSetCouple.getV2();
//        this.refreshUriRules(ruleWpSet);
//        LOG.info(Module.HTTP_GATEWAY, "初始化完毕,初始加载得到的列表信息为:{}", this.serverMetas);
//        nodeDiscovery.registerListen(new InstanceHooker());
//
//        this.schedualRefresh();
//    }
//
//    // 注册自身,网关为硬编码注册
//    private void registerSelf(InitCTX ctx)
//    {
//        String domain = (String) ctx.getData().get("domain");
//        String cluster = (String) ctx.getData().get("cluster");
//        String ip = (String) ctx.getData().get("ip");
//        Integer port = (Integer) ctx.getData().get("port");
//        String serviceName = (String) ctx.getData().get("serviceName");
//        ServerMetaData serverMetaData = new ServerMetaData();
//        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
//        extraInfo.setDomain(domain);
//        serverMetaData.setExtraInfo(extraInfo);
//        Map<String, String> metadatas = ServerMetaData.toMetaData(serverMetaData);
//        Instance instance = Instance.builder()
//                .weight((byte) 1)
//                .metaData(metadatas)
//                .clusterName(cluster)
//                .ip(ip)
//                .healthy(true)
//                .enable(true)
//                .port(port)
//                .serviceName(serviceName)
//                .build();
//        nodeDiscovery.registerServerInstance(instance);
//    }
//
//    private void schedualRefresh()
//    {
//        Flux.interval(Duration.ofMinutes(5)).map(v ->
//        {
//            Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster);
//            return serverInstanceList;
//        }).subscribe(serverInstanceList ->
//        {
//            if (serverInstanceList.size() == 0)
//            {
//                return;
//            }
//            synchronized (this.delta)
//            {
//                for (String s : serverInstanceList.keySet())
//                {
//                    List<Instance> instances = serverInstanceList.get(s);
//                    this.delta.put(instances.get(0).getClusterName(), instances);
//                }
//                this.onChange = true;
//            }
//        });
//    }
//
//    private Couple<Map<String, List<ServerCmdMetaInfo>>, Set<RuleWp>> conv()
//    {
//        Map<String, List<Instance>> cellInstance = this.delta;
//        return convCellInstanceToGateMeta(cellInstance);
//    }
//
//    class RuleWp
//    {
//        String method;
//        String protocol;
//    }
//
//    private Couple<Map<String, List<ServerCmdMetaInfo>>, Set<RuleWp>> convCellInstanceToGateMeta(Map<String, List<Instance>> m)
//    {
//        Set<String> keys = m.keySet();
//        Map<String, List<ServerCmdMetaInfo>> metas = new HashMap<>();
//        Set<RuleWp> ruleWps = new HashSet<>();
//
//        keys.stream().forEach(k ->
//        {
//            List<Instance> instances = m.get(k);
//            instances.stream().forEach(inst ->
//                    {
//                        ServerMetaInfo info = LBUtils.fromInstance(inst);
//                        ServerMetaData metaData = info.getMetaData();
//
//                        List<ServerMetaData.ServerMetaReactor> reactors = metaData.getReactors();
//                        if (CollectionUtils.isEmpty(reactors)) return;
//                        reactors.stream().forEach(r ->
//                        {
//                            List<ServerMetaData.ServerMetaCmd> cmds = r.getCmds();
//                            if (CollectionUtils.isEmpty(cmds)) return;
//                            cmds.stream().forEach(c ->
//                            {
//                                RuleWp wp = new RuleWp();
//                                wp.method = EnumHttpRequestType.getStrById(c.getMethod());
//                                wp.protocol = c.getProtocol();
//                                ruleWps.add(wp);
//
//                                String key = this.resolver.resolve(DefaultStringKeyResolver.StringKeyResolver.builder().uri(c.getProtocol()).method(wp.method).build());
//                                List<ServerCmdMetaInfo> serverMetaInfos = metas.get(key);
//                                if (CollectionUtils.isEmpty(serverMetaInfos))
//                                {
//                                    serverMetaInfos = new ArrayList<>();
//                                    metas.put(key, serverMetaInfos);
//                                }
//                                ServerCmdMetaInfo serverCmdMetaInfo = ServerCmdMetaInfo.fromServerMetaInfo(info, c.getModule());
//                                serverMetaInfos.add(serverCmdMetaInfo);
//                            });
//                        });
//                    }
//            );
//        });
//        return new Couple<>(metas, ruleWps);
//    }
//
//
//    private class InstanceHooker extends Subscriber<InstancesChangeEvent>
//    {
//        @Override
//        public void onEvent(InstancesChangeEvent event)
//        {
//            LOG.info(Module.HTTP_GATEWAY, "收到event:{},hosts:{}", event);
//            // FIXME , 处理nacos 的cluster
//            String clusters = event.getClusters();
//
//            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts().stream().filter(e ->
//                    e.getClusterName().equalsIgnoreCase(ServiceDiscovery.this.cluster)).collect(Collectors.toList());
//            synchronized (ServiceDiscovery.this.delta)
//            {
//                ServiceDiscovery.this.delta.put(event.getServiceName(), DiscoveryUtils.convNaocsInstance2CellInstance(hosts));
//                ServiceDiscovery.this.onChange = true;
//            }
//        }
//
//        @Override
//        public Class<? extends Event> subscribeType()
//        {
//            return InstancesChangeEvent.class;
//        }
//    }
//
//    @Data
//    class InstanceWrapper
//    {
//        private String serviceName;
//        private com.alibaba.nacos.api.naming.pojo.Instance instance;
//        // true : add ,false: remove
//        private boolean add;
//    }
//
//    private class DefaultNacosLoadBalance implements ILoadBalancer
//    {
//        public DefaultNacosLoadBalance(ILoadBalancerStrategy strategy)
//        {
//            this.strategy = strategy;
//        }
//
//        private ILoadBalancerStrategy strategy;
//
//        @Override
//        public ServerCmdMetaInfo choseServer(List<ServerCmdMetaInfo> servers, String method, String uri)
//        {
//            return this.strategy.choseServer(ServiceDiscovery.getInstance().getServerByUri(method, uri), uri);
//        }
//    }
//
////    private void customizeApiDefinitions()
////    {
////        Set<ApiDefinition> definitions = new HashSet<>();
////        ApiDefinition api1 = new ApiDefinition("some_customized_api")
////                .setPredicateItems(new HashSet<ApiPredicateItem>()
////                {{
////                    add(new ApiPathPredicateItem().setPattern("/ahas"));
////                    add(new ApiPathPredicateItem().setPattern("/product/**")
////                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
////                }});
////        ApiDefinition api2 = new ApiDefinition("another_customized_api")
////                .setPredicateItems(new HashSet<ApiPredicateItem>()
////                {{
////                    add(new ApiPathPredicateItem().setPattern("/**")
////                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
////                }});
////        definitions.add(api1);
////        definitions.add(api2);
////    }
//
//
//    private void refreshUriRules(Set<RuleWp> ruleWps)
//    {
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        for (RuleWp ruleWp : ruleWps)
//        {
//            GatewayFlowRule rule = GatewayUtils.createGatewayFlowRule(ruleWp.method, ruleWp.protocol);
//            rules.add(rule);
//        }
//        GatewayRuleManager.loadRules(rules);
//    }
//
//}