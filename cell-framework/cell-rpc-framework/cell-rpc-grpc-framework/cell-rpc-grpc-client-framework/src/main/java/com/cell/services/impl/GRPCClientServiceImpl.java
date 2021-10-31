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
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
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
    private BaseGrpcGrpc.BaseGrpcStub stub;
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
        ret = newPromise(anno);
        if (anno.async())
        {
            this.getNextExecutor().execute(() ->
            {
                this.fire(request, anno, ret);
            });
        } else
        {
            this.fire(request, anno, ret);
        }
//
        return ret;
    }

    private void fire(GrpcRequest request, GRPCClientRequestAnno anno, Promise<Object> promise)
    {

        this.stub.sendRequest(request, new StreamObserver<GrpcResponse>()
        {
            @Override
            public void onNext(GrpcResponse value)
            {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable t)
            {
                promise.tryFailure(t);
            }

            @Override
            public void onCompleted()
            {
                System.out.println("completed");
            }
        });
    }

    private EventExecutor getNextExecutor()
    {
        return this.eventExecutor.next();
    }

    private Promise<Object> newPromise(GRPCClientRequestAnno anno)
    {
        Promise<Object> ret;
        boolean async = anno.async();
        if (async)
        {
            ret = new BasePromise<>(this.eventExecutor);
        } else
        {
            ret = new BasePromise<>(DummyExecutor.getInstance());
        }
        byte l = anno.timeOut();
        if (l > 0)
        {
            this.timeWheel.addTask((t) ->
            {
                if (ret.isDone())
                {
                    return Mono.empty();
                }
                ret.tryFailure(new TimeoutException("asd"));
                return Mono.empty();
            }, TimeUnit.SECONDS, l);
        }
        return ret;
    }

}
