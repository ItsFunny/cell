package com.cell.services.impl;

import com.cell.annotation.GRPCClient;
import com.cell.annotation.GRPCClientRequestAnno;
import com.cell.concurrent.DummyExecutor;
import com.cell.concurrent.base.BasePromise;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.concurrent.base.Promise;
import com.cell.exceptions.ProgramaException;
import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
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
    @GRPCClient(
            "static://127.0.0.1:12000"
    )
    private BaseGrpcGrpc.BaseGrpcFutureStub stub;
    //    private BaseGrpcGrpc.BaseGrpcBlockingStub stub;
    private EventExecutor eventExecutor;

    private DefaultHashedTimeWheel timeWheel;

    // 时间轮


    public GRPCClientServiceImpl(EventExecutor eventExecutor)
    {
        this.eventExecutor = eventExecutor;
        this.timeWheel = DefaultHashedTimeWheel.getInstance();
    }


    public void send()
    {

    }


    @Override
    public Future<Object> call(ISerializable req)
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
//        try
//        {
//            GrpcResponse grpcResponse = this.stub.sendRequest(request);
//        }catch (Exception e){
//
//        }
        Promise<Object> promise;
        boolean async = anno.async();
        EventExecutor e;
        if (async)
        {
            e = this.eventExecutor;
        } else
        {
            e = DummyExecutor.getInstance();
        }
        promise = new BasePromise<>(e);
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

        if (async)
        {
            this.getNextExecutor().execute(() ->
                    this.call(request, anno, promise, e));
        } else
        {
            this.call(request, anno, promise, e);
        }

        return promise;
    }

    private void call(GrpcRequest request, GRPCClientRequestAnno anno, Promise<Object> promise, EventExecutor eventExecutor)
    {
        try
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
            }, eventExecutor);
//            GrpcResponse response = this.stub.sendRequest(request);
//            promise.trySuccess(this.getRet(response, anno));
        } catch (Exception ee)
        {
            promise.tryFailure(ee);
        }
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

    private EventExecutor getNextExecutor()
    {
        return this.eventExecutor.next();
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
