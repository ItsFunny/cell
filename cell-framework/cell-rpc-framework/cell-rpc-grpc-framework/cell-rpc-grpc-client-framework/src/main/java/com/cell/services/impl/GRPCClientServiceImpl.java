package com.cell.services.impl;

import com.cell.annotation.GRPCClient;
import com.cell.annotation.GRPCClientRequestAnno;
import com.cell.cluster.BaseGrpcGrpc;
import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.base.*;
import com.cell.exceptions.ProgramaException;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
import com.cell.protocol.IBuzzContext;
import com.cell.serialize.ISerializable;
import com.cell.services.IGRPCClientService;
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
 * @Date 创建时间：2021-10-28 16:35
 */

public class GRPCClientServiceImpl implements IGRPCClientService
{
    // TODO, discovery
    @GRPCClient(
            "static://127.0.0.1:12000"
    )
    private BaseGrpcGrpc.BaseGrpcFutureStub stub;
    //    private BaseGrpcGrpc.BaseGrpcBlockingStub stub;
    private EventLoopGroup group;

    private DefaultHashedTimeWheel timeWheel;

    // 时间轮

    public GRPCClientServiceImpl(EventLoopGroup group)
    {
        this.group = group;
        this.timeWheel = DefaultHashedTimeWheel.getInstance();
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
        return this.fire(request, anno);
    }

    private Future<Object> fire(GrpcRequest request, GRPCClientRequestAnno anno)
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
                    ListenableFuture<GrpcResponse> future = this.stub.sendRequest(request);
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
                ListenableFuture<GrpcResponse> future = this.stub.sendRequest(request);
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

    private void async()
    {
//        ListenableFuture<GrpcResponse> responseListenableFuture = this.stub.sendRequest(request);
//        Futures.addCallback(responseListenableFuture, new FutureCallback<GrpcResponse>()
//        {
//            @Override
//            public void onSuccess(@NullableDecl GrpcResponse result)
//            {
//                // TODO verify
//                Class<? extends ISerializable> aClass = anno.responseType();
//                ISerializable ret = null;
//                try
//                {
//                    ret = aClass.newInstance();
//                    ret.fromBytes(result.getData().toByteArray());
//                    promise.trySuccess(ret);
//                } catch (Exception e)
//                {
//                    promise.tryFailure(new ProgramaException(e));
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t)
//            {
//                promise.tryFailure(t);
//            }
//        }, e);
    }

    private EventExecutor getExecutor()
    {
        return this.group.next();
    }
//    private Couple<Promise<Object>, EventExecutor> newPromise(GRPCClientRequestAnno anno)
//    {
//        Promise<Object> ret;
//        EventExecutor e;
//        boolean async = anno.async();
//        if (async)
//        {
//            ret = new BasePromise<>(this.eventExecutor);
//            e = this.eventExecutor;
//        } else
//        {
//            ret = new BasePromise<>(DummyExecutor.getInstance());
//            e = DummyExecutor.getInstance();
//        }
//        byte l = anno.timeOut();
//        if (l > 0)
//        {
//            this.timeWheel.addTask((t) ->
//            {
//                if (ret.isDone())
//                {
//                    return Mono.empty();
//                }
//                ret.tryFailure(new TimeoutException("asd"));
//                return Mono.empty();
//            }, TimeUnit.SECONDS, l);
//        }
//        return new Couple<>(ret, e);
//    }

}
