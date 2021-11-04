package com.cell.discovery.nacos.grpc.client.server;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.annotations.AutoPlugin;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.loadbalance.utils.LBUtils;
import com.cell.cluster.BaseGrpcGrpc;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.grpc.client.base.framework.server.AbstractGRPCClientServer;
import com.cell.lb.ILoadBalancer;
import com.cell.model.Instance;
import com.cell.models.Couple;
import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultStringKeyResolver;
import com.cell.transport.model.ServerMetaData;

import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 22:10
 */
public class DefaultGrpcNacosClientServer extends AbstractGRPCClientServer implements IGRPCNacosClientServer
{
    public DefaultGrpcNacosClientServer(EventLoopGroup group)
    {
        super(group);
    }

    private static DefaultGrpcNacosClientServer instance;
    private Map<String, BaseGrpcGrpc.BaseGrpcFutureStub> stubs = new HashMap<>();

    private INacosNodeDiscovery nodeDiscovery;
    private ILoadBalancer loadBalancer;
    private Map<String, List<ServerCmdMetaInfo>> serverMetas = new HashMap<>();
    private IKeyResolver<DefaultStringKeyResolver.StringKeyResolver, String> resolver;
    private final Map<String, List<Instance>> delta = new HashMap<>();
    private volatile boolean onChange = false;
    private String cluster;

    @AutoPlugin
    public void setInstance(DefaultGrpcNacosClientServer server)
    {
        DefaultGrpcNacosClientServer.instance = server;
    }

    public static DefaultGrpcNacosClientServer getInstance()
    {
        return instance;
    }


    @Override
    protected BaseGrpcGrpc.BaseGrpcFutureStub getStub(String protocol)
    {
        this.transferIfNeed();
        return this.stubs.get(protocol);
    }

    private void transferIfNeed()
    {
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.resolver = new DefaultStringKeyResolver();
        this.nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();


        Map<String, List<Instance>> serverInstanceList = nodeDiscovery.getServerInstanceList(this.cluster);
        Map<String, List<ServerCmdMetaInfo>> instanceMap = convCellInstanceToGateMeta(serverInstanceList);

        Set<String> keySet = serverInstanceList.keySet();
        for (String s : keySet)
        {
            List<ServerCmdMetaInfo> serverCmdMetaInfos = instanceMap.get(s);

        }


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
                        Couple<ServerMetaInfo, ServerMetaData> couple = LBUtils.fromInstance(inst);
                        ServerMetaInfo info = couple.getV1();
                        ServerMetaData metaData = couple.getV2();

                        List<ServerMetaData.ServerMetaReactor> reactors = metaData.getReactors();
                        if (CollectionUtils.isEmpty(reactors)) return;
                        reactors.stream().forEach(r ->
                        {
                            List<ServerMetaData.ServerMetaCmd> cmds = r.getCmds();
                            if (CollectionUtils.isEmpty(cmds)) return;
                            cmds.stream().forEach(c ->
                            {
                                String key = this.resolver.resolve(DefaultStringKeyResolver.StringKeyResolver.builder()
                                        .uri(c.getProtocol()).build());
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
}
