package com.cell.discovery.nacos.grpc.client.server;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.annotations.AutoPlugin;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.loadbalance.utils.LBUtils;
import com.cell.cluster.BaseGrpcGrpc;
import com.cell.com.cell.grpc.common.constants.GRPCConstants;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.discovery.nacos.grpc.client.discovery.GRPCClientServiceDiscovery;
import com.cell.discovery.nacos.grpc.client.extension.GRPCClientDiscoveryExtension;
import com.cell.discovery.nacos.grpc.client.keyresolver.RPCKeyResolver;
import com.cell.discovery.services.IInstanceOnChange;
import com.cell.grpc.client.autoconfigurer.config.GRPCClientConfiguration;
import com.cell.grpc.client.base.framework.server.AbstractGRPCClientServer;
import com.cell.lb.ILoadBalancer;
import com.cell.model.Instance;
import com.cell.models.Couple;
import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultStringKeyResolver;
import com.cell.root.Root;
import com.cell.transport.model.ServerMetaData;
import com.cell.util.GRPCUtil;
import io.grpc.stub.AbstractStub;

import java.net.URI;
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

    private GRPCClientServiceDiscovery grpcClientServiceDiscovery;


    @AutoPlugin
    public void setGrpcClientServiceDiscovery(GRPCClientServiceDiscovery grpcClientServiceDiscovery)
    {
        this.grpcClientServiceDiscovery = grpcClientServiceDiscovery;
    }


    @Override
    protected BaseGrpcGrpc.BaseGrpcFutureStub getStub(String protocol)
    {
        ServerCmdMetaInfo serverCmdMetaInfo = this.grpcClientServiceDiscovery.choseServer(protocol);
        if (serverCmdMetaInfo == null)
        {
            return null;
        }
        return this.stubs.get(serverCmdMetaInfo.ID());
    }

    private void transferIfNeed()
    {
    }

    private Map<Integer, BaseGrpcGrpc.BaseGrpcFutureStub> stubs = new HashMap<>();

    @Override
    protected void onInit(InitCTX ctx)
    {
        ctx.getData().put(GRPCClientServiceDiscovery.callBackStr, (IInstanceOnChange) serverMetas ->
        {
            Collection<List<ServerCmdMetaInfo>> values = serverMetas.values();
            for (List<ServerCmdMetaInfo> value : values)
            {
                for (ServerCmdMetaInfo serverCmdMetaInfo : value)
                {
                    GRPCClientConfiguration.GRPCClientConfigurationNode node = new GRPCClientConfiguration.GRPCClientConfigurationNode();
                    String ip = serverCmdMetaInfo.getIp();
                    short port = serverCmdMetaInfo.getPort();
                    node.setAddress(URI.create("static://" + ip + ":" + port));
                    int code = serverCmdMetaInfo.ID();
                    String stubName = code + "";
                    GRPCClientConfiguration.getInstance().updateRuntime(stubName, node);
                    AbstractStub<?> stub = GRPCUtil.createaaStub(Root.getApplicationContext(),
                            (Class<? extends AbstractStub<?>>) BaseGrpcGrpc.BaseGrpcFutureStub.class.asSubclass(AbstractStub.class),
                            stubName,
                            new ArrayList<>(),
                            false);
                    this.stubs.put(code, (BaseGrpcGrpc.BaseGrpcFutureStub) stub);
                }
            }
        });
        this.grpcClientServiceDiscovery.initOnce(ctx);
    }
}
