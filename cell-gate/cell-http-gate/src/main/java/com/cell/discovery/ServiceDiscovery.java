package com.cell.discovery;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.annotations.AutoPlugin;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.lb.ILoadBalancer;
import com.cell.lb.ILoadBalancerStrategy;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.model.ServerMetaInfo;
import com.cell.models.Module;
import com.cell.transport.model.ServerMetaData;
import com.cell.util.DiscoveryUtils;
import lombok.Data;

import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:41
 */
@Data
public class ServiceDiscovery extends AbstractInitOnce
{
    private static ServiceDiscovery instance;

    private INacosNodeDiscovery nodeDiscovery;

    private ILoadBalancer loadBalancer;
    private Map<String, List<ServerMetaInfo>> serverMetas = new HashMap<>();
    private final Map<String, List<com.alibaba.nacos.api.naming.pojo.Instance>> delta = new HashMap<>();
    private String cluster;

    @AutoPlugin
    public void setInstance(ServiceDiscovery serviceDiscovery)
    {
        ServiceDiscovery.instance = serviceDiscovery;
    }

    @AutoPlugin
    private void setLoadBalancerStrategy(ILoadBalancerStrategy strategy)
    {
        this.loadBalancer = new DefaultNacosLoadBalance(strategy);
    }


    public static ServiceDiscovery getInstance()
    {
        return instance;
    }

    private List<ServerMetaInfo> getServerByUri(String uri)
    {
        this.transferIfNeed();
        return this.serverMetas.get(uri);
    }

    public ServerMetaInfo choseServer(String uri)
    {
        return this.loadBalancer.choseServer(this.getServerByUri(uri), uri);
    }

    public synchronized Map<String, List<com.alibaba.nacos.api.naming.pojo.Instance>> getCurrentDelta()
    {
        return new HashMap<>(this.delta);
    }

    private void transferIfNeed()
    {
        if (this.delta.isEmpty()) return;
        // index:0
        Map<String, List<List<ServerMetaInfo>>> compareChanges = new HashMap<>();
        final List<List<ServerMetaInfo>> dels = new ArrayList<>();
        synchronized (this.delta)
        {
            if (this.delta.isEmpty()) return;
            Set<String> serviceNames = this.delta.keySet();
            serviceNames.stream().forEach(n ->
            {
                List<com.alibaba.nacos.api.naming.pojo.Instance> instances = this.delta.get(n);
                if (CollectionUtils.isEmpty(instances))
                {
                    dels.add(this.serverMetas.remove(n));
                    this.delta.remove(n);
                    return;
                }
                Map<String, List<ServerMetaInfo>> conv = this.conv();
                Set<String> changes = conv.keySet();
                changes.stream().forEach(name ->
                {
                    List<ServerMetaInfo> metas = conv.get(name);
                    List<ServerMetaInfo> origin = this.serverMetas.get(name);
                    this.serverMetas.put(name, metas);
                    List<List<ServerMetaInfo>> r = new ArrayList<>();
                    r.add(origin);
                    r.add(metas);
                    compareChanges.put(name, r);
                });
            });
            this.delta.clear();
        }
        LOG.info(Module.HTTP_GATEWAY, "删除的router:{},变更的信息:{}", dels, compareChanges);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList();
        this.serverMetas = convCellInstanceToGateMeta(serverInstanceList);
        LOG.info(Module.HTTP_GATEWAY, "初始化完毕,初始加载得到的列表信息为:{}", this.serverMetas);
        nodeDiscovery.registerListen(new InstanceHooker());
    }

    private Map<String, List<ServerMetaInfo>> conv()
    {
        Map<String, List<Instance>> cellInstance = DiscoveryUtils.convNacosMapInstanceToCellInstance(this.delta);
        return convCellInstanceToGateMeta(cellInstance);
    }

    private Map<String, List<ServerMetaInfo>> convCellInstanceToGateMeta(Map<String, List<Instance>> m)
    {
        Set<String> keys = m.keySet();
        Map<String, List<ServerMetaInfo>> metas = new HashMap<>();
        keys.stream().forEach(k ->
        {
            List<Instance> instances = m.get(k);
            instances.stream().forEach(inst ->
                    {
                        final ServerMetaInfo info = new ServerMetaInfo();
                        info.setIp(inst.getIp());
                        info.setPort(Short.valueOf(String.valueOf(inst.getPort())));
                        info.setServiceName(k);

                        ServerMetaData metaData = ServerMetaData.fromMetaData(inst.getMetaData());
                        List<ServerMetaData.ServerMetaReactor> reactors = metaData.getReactors();
                        if (CollectionUtils.isEmpty(reactors)) return;
                        reactors.stream().forEach(r ->
                        {
                            List<ServerMetaData.ServerMetaCmd> cmds = r.getCmds();
                            if (CollectionUtils.isEmpty(cmds)) return;
                            cmds.stream().forEach(c ->
                            {
                                String uri = c.getUri();
                                List<ServerMetaInfo> serverMetaInfos = metas.get(uri);
                                if (CollectionUtils.isEmpty(serverMetaInfos))
                                {
                                    serverMetaInfos = new ArrayList<>();
                                    metas.put(uri, serverMetaInfos);
                                }
                                serverMetaInfos.add(info);
                            });
                        });
                    }
            );
        });
        return metas;
    }


    private class InstanceHooker extends Subscriber<InstancesChangeEvent>
    {
        @Override
        public void onEvent(InstancesChangeEvent event)
        {
//            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts();
            LOG.info(Module.HTTP_GATEWAY, "收到event:{},hosts:{}", event);
            // FIXME , 处理nacos 的cluster
            String clusters = event.getClusters();


            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts();
            synchronized (ServiceDiscovery.this.delta)
            {
                ServiceDiscovery.this.delta.put(event.getServiceName(), hosts);
            }
        }

        @Override
        public Class<? extends Event> subscribeType()
        {
            return InstancesChangeEvent.class;
        }
    }

    @Data
    class InstanceWrapper
    {
        private String serviceName;
        private com.alibaba.nacos.api.naming.pojo.Instance instance;
        // true : add ,false: remove
        private boolean add;
    }

    private class DefaultNacosLoadBalance implements ILoadBalancer
    {
        public DefaultNacosLoadBalance(ILoadBalancerStrategy strategy)
        {
            this.strategy = strategy;
        }

        private ILoadBalancerStrategy strategy;

        @Override
        public ServerMetaInfo choseServer(List<ServerMetaInfo> servers, String uri)
        {
            return this.strategy.choseServer(ServiceDiscovery.getInstance().getServerByUri(uri), uri);
        }
    }
}