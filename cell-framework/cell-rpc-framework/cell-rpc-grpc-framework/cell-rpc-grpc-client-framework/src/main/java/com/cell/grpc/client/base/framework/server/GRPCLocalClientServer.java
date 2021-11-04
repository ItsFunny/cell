package com.cell.grpc.client.base.framework.server;

import com.cell.cluster.BaseGrpcGrpc;
import com.cell.com.cell.grpc.common.config.GRPCServerConfiguration;
import com.cell.com.cell.grpc.common.constants.GRPCConstants;
import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.base.*;
import com.cell.configuration.RootConfiguration;
import com.cell.context.InitCTX;
import com.cell.exceptions.ProgramaException;
import com.cell.grpc.client.base.framework.annotation.GRPCClientRequestAnno;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
import com.cell.protocol.IBuzzContext;
import com.cell.proxy.IFrameworkProxy;
import com.cell.root.Root;
import com.cell.rpc.client.base.server.AbstractRPCClientServer;
import com.cell.serialize.ISerializable;
import com.cell.timewheel.DefaultHashedTimeWheel;
import com.cell.util.GRPCUtil;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import io.grpc.stub.AbstractStub;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        AbstractStub<?> abstractStub = GRPCUtil.createaaStub(Root.getApplicationContext(),
                (Class<? extends AbstractStub<?>>) BaseGrpcGrpc.BaseGrpcFutureStub.class.asSubclass(AbstractStub.class),
                GRPCConstants.DEFAULT_GRPC_SERVER,
                new ArrayList<>(),
                false);
        this.stub = (BaseGrpcGrpc.BaseGrpcFutureStub) abstractStub;
    }
}
