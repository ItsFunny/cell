package com.cell.rpc.grpc.client.framework.server;

import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.context.InitCTX;
import com.cell.grpc.client.autoconfigurer.config.GRPCClientConfiguration;
import com.cell.grpc.common.cluster.BaseGrpcGrpc;
import com.cell.grpc.common.constants.GRPCConstants;
import com.cell.root.Root;
import com.cell.util.GRPCUtil;
import io.grpc.stub.AbstractStub;

import java.net.URI;
import java.util.ArrayList;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 15:37
 */
public class GRPCLocalClientServer extends AbstractGRPCClientServer implements ILocalGRPCClientServer
{
    private BaseGrpcGrpc.BaseGrpcFutureStub stub;

    public GRPCLocalClientServer(EventLoopGroup group)
    {
        super(group);
    }


    @Override
    protected BaseGrpcGrpc.BaseGrpcFutureStub getStub(String protocol)
    {
        return this.stub;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        // TODO,server 名字需要从配置文件中读取
        GRPCClientConfiguration.GRPCClientConfigurationNode node = new GRPCClientConfiguration.GRPCClientConfigurationNode();
        node.setAddress(URI.create("static://" + GRPCConstants.DEFAULT_GRPC_SERVER_ADDR + ":" + GRPCConstants.DEFAULT_GRPC_SERVER_PORT));
        GRPCClientConfiguration.getInstance().updateRuntime(GRPCConstants.DEFAULT_GRPC_SERVER, node);
        AbstractStub<?> abstractStub = GRPCUtil.createaaStub(Root.getApplicationContext(),
                (Class<? extends AbstractStub<?>>) BaseGrpcGrpc.BaseGrpcFutureStub.class.asSubclass(AbstractStub.class),
                GRPCConstants.DEFAULT_GRPC_SERVER,
                new ArrayList<>(),
                false);
        this.stub = (BaseGrpcGrpc.BaseGrpcFutureStub) abstractStub;
    }
}
