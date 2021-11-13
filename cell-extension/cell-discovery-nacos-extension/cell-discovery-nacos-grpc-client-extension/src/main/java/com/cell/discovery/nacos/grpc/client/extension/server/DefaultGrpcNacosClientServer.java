package com.cell.discovery.nacos.grpc.client.extension.server;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.common.context.InitCTX;
import com.cell.discovery.nacos.grpc.client.extension.discovery.GRPCClientServiceDiscovery;
import com.cell.grpc.common.cluster.BaseGrpcGrpc;
import com.cell.node.discovery.nacos.discovery.IInstanceOnChange;
import com.cell.node.discovery.nacos.discovery.IServiceDiscovery;
import com.cell.rpc.grpc.client.grpc.client.autoconfigurer.config.GRPCClientConfiguration;
import com.cell.rpc.grpc.client.util.GRPCUtil;
import com.cell.sdk.log.LOG;
import com.cell.base.common.models.Module;
import com.cell.base.framework.root.Root;
import com.cell.rpc.grpc.client.framework.server.AbstractGRPCClientServer;
import io.grpc.stub.AbstractStub;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private IServiceDiscovery grpcClientServiceDiscovery;


    @AutoPlugin
    public void setGrpcClientServiceDiscovery(GRPCClientServiceDiscovery grpcClientServiceDiscovery)
    {
        this.grpcClientServiceDiscovery = grpcClientServiceDiscovery;
    }


    @Override
    protected BaseGrpcGrpc.BaseGrpcFutureStub getStub(String protocol)
    {
        ServerCmdMetaInfo serverCmdMetaInfo = this.grpcClientServiceDiscovery.choseServer(null, protocol);
        if (serverCmdMetaInfo == null)
        {
            return null;
        }
        this.rwMtx.readLock().lock();
        BaseGrpcGrpc.BaseGrpcFutureStub stub = this.stubs.get(serverCmdMetaInfo.ID());
        this.rwMtx.readLock().unlock();
        return stub;
    }

    private ReentrantReadWriteLock rwMtx = new ReentrantReadWriteLock();
    //    private Map<Integer, TargetWrapper> stubs = new HashMap<>();
    private Map<Integer, BaseGrpcGrpc.BaseGrpcFutureStub> stubs = new HashMap<>();
    // 存储的是 instance -对应的stub ,一个instance 有多个 cmdInfo,但是只会有一个stub
    private Map<String, BaseGrpcGrpc.BaseGrpcFutureStub> targetStubs = new HashMap<>();


    class TargetWrapper
    {
        String key;
        BaseGrpcGrpc.BaseGrpcFutureStub stub;

        public TargetWrapper(String key, BaseGrpcGrpc.BaseGrpcFutureStub stub)
        {
            this.key = key;
            this.stub = stub;
        }
    }


    @Override
    protected void onInit(InitCTX ctx)
    {
        ctx.getData().put(GRPCClientServiceDiscovery.callBackStr, (IInstanceOnChange) snap ->
        {
            Map<String, Set<ServerCmdMetaInfo>> newProtocols = snap.getNewProtocols();
            Map<String, Set<ServerCmdMetaInfo>> deltaAddProtocols = snap.getDeltaAddProtocols();
            Map<String, Set<ServerCmdMetaInfo>> downProtocols = snap.getDownProtocols();
            Map<String, Set<ServerCmdMetaInfo>> deltaDownProtocols = snap.getDeltaDownProtocols();
            try
            {
                this.rwMtx.writeLock().lock();
                // 处理新增
                LOG.info(Module.GRPC_SERVER, "开始处理新增:{}", newProtocols);
                this.handleAdd(newProtocols);
                LOG.info(Module.GRPC_SERVER, "开始处理增量新增:{}", deltaAddProtocols);
                this.handleAdd(deltaAddProtocols);
                LOG.info(Module.GRPC_SERVER, "开始处理宕机的protocol:{}", downProtocols);
                this.handleDown(downProtocols, true);
                this.handleDown(deltaDownProtocols, false);
            } finally
            {
                this.rwMtx.writeLock().unlock();
            }
        });
        this.grpcClientServiceDiscovery.initOnce(ctx);
    }

    private void handleAdd(Map<String, Set<ServerCmdMetaInfo>> protocols)
    {
        Set<String> protocolKeys = protocols.keySet();
        for (String protocol : protocolKeys)
        {
            Set<ServerCmdMetaInfo> serverCmdMetaInfos = protocols.get(protocol);
            for (ServerCmdMetaInfo serverCmdMetaInfo : serverCmdMetaInfos)
            {
                String staticByInfo = this.createStaticByInfo(serverCmdMetaInfo);
                BaseGrpcGrpc.BaseGrpcFutureStub stub = this.targetStubs.get(staticByInfo);
                if (stub == null)
                {
                    stub = this.createStub(staticByInfo);
                    this.targetStubs.put(staticByInfo, stub);
                }
                int code = serverCmdMetaInfo.ID();
                this.stubs.put(code, stub);
                LOG.info(Module.GRPC_SERVER, "add stub,code={}", code);
            }
        }
    }

    // TODO optimize ,对于down的protocol,需要重新定义结构体,有哪些
    private void handleDown(Map<String, Set<ServerCmdMetaInfo>> protocols, boolean closeStub)
    {
        Set<String> protocolKeys = protocols.keySet();
        for (String protocolKey : protocolKeys)
        {
            // 1. 删除code
            // 2. 断开protocol
            Set<ServerCmdMetaInfo> serverCmdMetaInfos = protocols.get(protocolKey);
            if (CollectionUtils.isEmpty(serverCmdMetaInfos)) continue;
            String staticKey = "";
            for (ServerCmdMetaInfo serverCmdMetaInfo : serverCmdMetaInfos)
            {
                staticKey = this.createStaticByInfo(serverCmdMetaInfo);
                int code = serverCmdMetaInfo.ID();
                this.stubs.remove(code);
                LOG.info(Module.GRPC_SERVER, "remove stub:{}", code);
            }
            if (closeStub)
            {
                this.targetStubs.remove(staticKey);
            }
        }
    }


    private BaseGrpcGrpc.BaseGrpcFutureStub createStub(String staticUri)
    {
        GRPCClientConfiguration.GRPCClientConfigurationNode node = new GRPCClientConfiguration.GRPCClientConfigurationNode();
        node.setAddress(URI.create(staticUri));
        GRPCClientConfiguration.getInstance().updateRuntime(staticUri, node);
        return (BaseGrpcGrpc.BaseGrpcFutureStub) GRPCUtil.createaaStub(Root.getApplicationContext(),
                (Class<? extends AbstractStub<?>>) BaseGrpcGrpc.BaseGrpcFutureStub.class.asSubclass(AbstractStub.class),
                staticUri,
                new ArrayList<>(),
                false);
    }


    private String createStaticByInfo(ServerCmdMetaInfo info)
    {
        return "static://" + info.getIp() + ":" + info.getPort();
    }

}
