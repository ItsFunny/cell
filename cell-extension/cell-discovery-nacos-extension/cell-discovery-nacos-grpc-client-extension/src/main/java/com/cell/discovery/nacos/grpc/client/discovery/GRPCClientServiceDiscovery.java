package com.cell.discovery.nacos.grpc.client.discovery;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.annotations.AutoPlugin;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.loadbalance.utils.LBUtils;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.discovery.nacos.grpc.client.keyresolver.RPCKeyResolver;
import com.cell.discovery.services.IInstanceOnChange;
import com.cell.exceptions.ConfigException;
import com.cell.lb.ILoadBalancer;
import com.cell.lb.ILoadBalancerStrategy;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Module;
import com.cell.resolver.IKeyResolver;
import com.cell.transport.model.ServerMetaData;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 09:34
 */
public class GRPCClientServiceDiscovery extends AbstractInitOnce
{
    private static GRPCClientServiceDiscovery instance = null;
    public static final String callBackStr = "callback";

    private INacosNodeDiscovery nodeDiscovery;
    private String cluster;

    private IKeyResolver<String, String> resolver;
    private ILoadBalancer loadBalancer;
    private IInstanceOnChange callBack;

    private Map<String, List<ServerCmdMetaInfo>> serverMetas = new HashMap<>();
    private final Map<String, List<Instance>> delta = new HashMap<>();
    private volatile boolean onChange = false;


    @AutoPlugin
    public void setInstance(GRPCClientServiceDiscovery server)
    {
        GRPCClientServiceDiscovery.instance = server;
    }

    @AutoPlugin
    private void setLoadBalancerStrategy(ILoadBalancerStrategy strategy)
    {
        this.loadBalancer = new DefaultGRPCClientLoadBalancer(strategy);
    }

    public Map<String, List<ServerCmdMetaInfo>> getServerMetas()
    {
        return this.serverMetas;
    }

    public ServerCmdMetaInfo choseServer(String protocol)
    {
        return this.loadBalancer.choseServer(this.getServerByProtocol(protocol), "", protocol);
    }

    private List<ServerCmdMetaInfo> getServerByProtocol(String protocol)
    {
        this.transferIfNeed();
        // FIXME
        return this.serverMetas.get(protocol);
    }


    class DefaultGRPCClientLoadBalancer implements ILoadBalancer
    {
        private ILoadBalancerStrategy strategy;

        public DefaultGRPCClientLoadBalancer(ILoadBalancerStrategy strategy)
        {
            this.strategy = strategy;
        }

        @Override
        public ServerCmdMetaInfo choseServer(List<ServerCmdMetaInfo> servers, String method, String protocol)
        {
            return this.strategy.choseServer(servers, protocol);
        }
    }

    private void transferIfNeed()
    {
        if (!this.onChange) return;
        // index:0
        Map<String, List<List<ServerCmdMetaInfo>>> compareChanges = new HashMap<>();
        final List<List<ServerCmdMetaInfo>> dels = new ArrayList<>();
        synchronized (this.delta)
        {
            if (!this.onChange) return;
            Set<String> serviceNames = this.delta.keySet();
            serviceNames.stream().forEach(n ->
            {
                List<Instance> instances = this.delta.get(n);
                if (CollectionUtils.isEmpty(instances))
                {
                    dels.add(this.serverMetas.remove(n));
                    this.delta.remove(n);
                    this.serverMetas.remove(n);
                    return;
                }
                Map<String, List<ServerCmdMetaInfo>> conv = this.conv();
                Set<String> changes = conv.keySet();
                changes.stream().forEach(name ->
                {
                    List<ServerCmdMetaInfo> metas = conv.get(name);
                    List<ServerCmdMetaInfo> origin = this.serverMetas.get(name);
                    this.serverMetas.put(name, metas);
                    List<List<ServerCmdMetaInfo>> r = new ArrayList<>();
                    r.add(origin);
                    r.add(metas);
                    compareChanges.put(name, r);
                });
            });
            // TODO EXCEPTION CATCH
            this.callBack.onChange(this.serverMetas);
            this.delta.clear();
            this.onChange = false;
        }
        LOG.info(Module.HTTP_GATEWAY, "删除的router:{},变更的信息:{}", dels, compareChanges);
    }

    private Map<String, List<ServerCmdMetaInfo>> conv()
    {
        Map<String, List<Instance>> cellInstance = this.delta;
        return convCellInstanceToGateMeta(cellInstance);
    }

    protected byte filterType()
    {
        return ProtocolConstants.TYPE_RPC;
    }

    private Map<String, List<ServerCmdMetaInfo>> convCellInstanceToGateMeta(Map<String, List<Instance>> m)
    {
        Set<String> keys = m.keySet();
        Map<String, List<ServerCmdMetaInfo>> metas = new HashMap<>();

        keys.stream().forEach(k ->
        {
            List<Instance> instances = m.get(k);
            instances.stream().forEach(inst ->
                    {
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
                                String key = this.resolver.resolve(c.getProtocol());
                                List<ServerCmdMetaInfo> serverMetaInfos = metas.get(key);
                                if (CollectionUtils.isEmpty(serverMetaInfos))
                                {
                                    serverMetaInfos = new ArrayList<>();
                                    metas.put(key, serverMetaInfos);
                                }
                                ServerCmdMetaInfo serverCmdMetaInfo = ServerCmdMetaInfo.fromServerMetaInfo(info, c.getModule());
                                serverMetaInfos.add(serverCmdMetaInfo);
                            });
                        });
                    }
            );
        });
        return metas;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.resolver = new RPCKeyResolver();
        this.nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();

        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster);
        this.serverMetas = convCellInstanceToGateMeta(serverInstanceList);
        this.schedualRefresh();
        IInstanceOnChange c = (IInstanceOnChange) ctx.getData().get(callBackStr);
        if (c == null)
        {
            throw new ConfigException("asd");
        }
        c.onChange(this.serverMetas);
    }

    private void schedualRefresh()
    {
        Flux.interval(Duration.ofMinutes(5)).map(v ->
        {
            Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster);
            return serverInstanceList;
        }).subscribe(serverInstanceList ->
        {
            if (serverInstanceList.size() == 0)
            {
                return;
            }
            synchronized (this.delta)
            {
                for (String s : serverInstanceList.keySet())
                {
                    List<Instance> instances = serverInstanceList.get(s);
                    this.delta.put(instances.get(0).getClusterName(), instances);
                }
                this.onChange = true;
            }
        });
    }

    public void setCluster(String cluster)
    {
        this.cluster = cluster;
    }


    public void setCallBack(IInstanceOnChange callBack)
    {
        this.callBack = callBack;
    }
}
