package com.cell.grpc.client.base.framework.server;

import com.cell.cluster.BaseGrpcGrpc;
import com.cell.com.cell.grpc.common.constants.GRPCConstants;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.context.InitCTX;
import com.cell.root.Root;
import com.cell.util.GRPCUtil;
import io.grpc.stub.AbstractStub;

import java.util.ArrayList;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 15:37
 */
public class GRPCLocalClientServer extends AbstractGRPCClientServer implements IGRPCClientServer
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
        AbstractStub<?> abstractStub = GRPCUtil.createaaStub(Root.getApplicationContext(),
                (Class<? extends AbstractStub<?>>) BaseGrpcGrpc.BaseGrpcFutureStub.class.asSubclass(AbstractStub.class),
                GRPCConstants.DEFAULT_GRPC_SERVER,
                new ArrayList<>(),
                false);
        this.stub = (BaseGrpcGrpc.BaseGrpcFutureStub) abstractStub;
    }
}
