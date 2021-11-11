package com.cell.rpc.grpc.client.framework.server;

import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.core.concurrent.DummyExecutor;
import com.cell.base.core.concurrent.base.*;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
import com.cell.grpc.common.cluster.BaseGrpcGrpc;
import com.cell.grpc.common.cluster.GrpcRequest;
import com.cell.grpc.common.cluster.GrpcResponse;
import com.cell.base.core.protocol.IBuzzContext;
import com.cell.rpc.client.base.framework.server.AbstractRPCClientServer;
import com.cell.rpc.grpc.client.framework.annotation.GRPCClientRequestAnno;
import com.cell.base.core.serialize.ISerializable;
import com.cell.timewheel.DefaultHashedTimeWheel;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 21:06
 */
public abstract class AbstractGRPCClientServer extends AbstractRPCClientServer implements IGRPCClientServer
{
    protected EventLoopGroup group;
    protected DefaultHashedTimeWheel timeWheel;

    public AbstractGRPCClientServer(EventLoopGroup group)
    {
        super();
        this.group = group;
        this.timeWheel = DefaultHashedTimeWheel.getInstance();
    }

    @Override
    protected void onStart()
    {

    }

    @Override
    protected void onShutdown()
    {

    }

    @Override
    public Future<Object> call(IBuzzContext context, ISerializable req)
    {
        // TODO OPTIMIZE ,编译时确定
        Promise<Object> ret;
        GRPCClientRequestAnno anno = req.getClass().getAnnotation(GRPCClientRequestAnno.class);
        if (anno == null)
        {
            ret = new BasePromise<>(DummyExecutor.getInstance());
            ret.setFailure(new ProgramaException("asd"));
            return ret;
        }
        String protocol = anno.protocol();
        final BaseGrpcGrpc.BaseGrpcFutureStub stub = this.getStub(protocol);
        if (stub == null)
        {
            ret = new BasePromise<>(DummyExecutor.getInstance());
            ret.setFailure(new ProgramaException("command not exist"));
            return ret;
        }

        byte[] bytes = null;
        try
        {
            bytes = req.toBytes();
        } catch (IOException e)
        {
            ret = new BasePromise<>(DummyExecutor.getInstance());
            ret.setFailure(e);
            return ret;
        }
        EnvelopeHeader envelopeHeader = EnvelopeHeader.newBuilder()
                .setProtocol(protocol)
                .setLength(bytes.length)
                .setSequenceId(context.getSummary().getSequenceId())
                .build();
        Payload payload = Payload.newBuilder()
                .setData(ByteString.copyFrom(bytes))
                .build();
        Envelope envelope = Envelope.newBuilder()
                .setHeader(envelopeHeader)
                .setPayload(payload)
                .build();
        GrpcRequest request = GrpcRequest.newBuilder().setEnvelope(envelope).build();
        return this.fire(stub, request, anno);
    }

    protected abstract BaseGrpcGrpc.BaseGrpcFutureStub getStub(String protocol);

    private Future<Object> fire(BaseGrpcGrpc.BaseGrpcFutureStub stub, GrpcRequest request, GRPCClientRequestAnno anno)
    {
        Promise<Object> promise = new BasePromise<>(DummyExecutor.getInstance());
        byte l = anno.timeOut();
        if (l > 0)
        {
            this.timeWheel.addTask((t) ->
            {
                if (promise.isDone())
                {
                    return Mono.empty();
                }
                promise.tryFailure(new TimeoutException("asd"));
                return Mono.empty();
            }, TimeUnit.SECONDS, l);
        }

        try
        {
            if (anno.async())
            {
                getExecutor().execute(() ->
                {
                    ListenableFuture<GrpcResponse> future = stub.sendRequest(request);
                    Futures.addCallback(future, new FutureCallback<GrpcResponse>()
                    {
                        @Override
                        public void onSuccess(@NullableDecl GrpcResponse result)
                        {
                            try
                            {
                                promise.trySuccess(getRet(result, anno));
                            } catch (Exception e)
                            {
                                promise.tryFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t)
                        {
                            promise.tryFailure(t);
                        }
                    }, this.getExecutor());
                });
            } else
            {
                ListenableFuture<GrpcResponse> future = stub.sendRequest(request);
                GrpcResponse response = future.get();
                promise.trySuccess(getRet(response, anno));
            }
        } catch (Exception ee)
        {
            promise.tryFailure(ee);
        }

        return promise;
    }


    private ISerializable getRet(GrpcResponse response, GRPCClientRequestAnno anno) throws Exception
    {
        Class<? extends ISerializable> aClass = anno.responseType();
        ISerializable ret = null;
        ret = aClass.newInstance();
        ret.fromBytes(response.getData().toByteArray());
        return ret;
    }

    protected EventExecutor getExecutor()
    {
        return this.group.next();
    }
}
