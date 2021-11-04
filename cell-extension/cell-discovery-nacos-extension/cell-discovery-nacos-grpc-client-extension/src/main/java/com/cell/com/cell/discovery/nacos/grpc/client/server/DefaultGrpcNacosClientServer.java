package com.cell.com.cell.discovery.nacos.grpc.client.server;

import com.cell.annotations.AutoPlugin;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.cluster.BaseGrpcGrpc;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.context.InitCTX;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.grpc.client.base.framework.server.AbstractGRPCClientServer;
import com.cell.model.Instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, List<ServerCmdMetaInfo>> serverMetas = new HashMap<>();
    private final Map<String, List<Instance>> delta = new HashMap<>();
    private volatile boolean onChange = false;
    private String cluster;

    @AutoPlugin
    public void setInstance(DefaultGrpcNacosClientServer server)
    {
        DefaultGrpcNacosClientServer.instance = server;
    }


    @Override
    protected BaseGrpcGrpc.BaseGrpcFutureStub getStub(String protocol)
    {
        return this.stubs.get(protocol);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {

    }
}
