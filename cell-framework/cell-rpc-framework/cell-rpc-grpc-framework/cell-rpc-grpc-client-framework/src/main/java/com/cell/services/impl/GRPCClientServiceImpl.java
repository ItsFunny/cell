package com.cell.services.impl;

import com.cell.annotation.GRPCClient;
import com.cell.annotation.GRPCClientRequestAnno;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Future;
import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeOrBuilder;
import com.cell.serialize.ISerializable;
import com.cell.serialize.ISerialize;
import com.cell.services.IGRPCClientRequest;
import com.cell.services.IGRPCClientService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // 时间轮


    public GRPCClientServiceImpl(EventExecutor eventExecutor)
    {
        this.eventExecutor = eventExecutor;
    }


    public void send()
    {

    }


    @Override
    public Future<Object> call(ISerializable req)
    {
        // TODO OPTIMIZE ,编译时确定
        GRPCClientRequestAnno anno = req.getClass().getAnnotation(GRPCClientRequestAnno.class);
        boolean async = anno.async();
        String protocol = anno.protocol();
        Class<?> aClass = anno.responseType();
        if (async){

        }

        Envelope.newBuilder().setPayload()
        GrpcRequest.newBuilder().setEnvelope()
        return null;
    }

}
