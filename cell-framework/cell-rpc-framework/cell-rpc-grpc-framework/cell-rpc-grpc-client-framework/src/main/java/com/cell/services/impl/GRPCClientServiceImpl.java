package com.cell.services.impl;

import com.cell.annotation.GRPCClient;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.concurrent.base.Future;
import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeOrBuilder;
import com.cell.services.IGRPCClientRequest;
import com.cell.services.IGRPCClientService;

import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 16:35
 */
@ActivePlugin
public class GRPCClientServiceImpl implements IGRPCClientService
{
    @GRPCClient(
            "static://127.0.0.1:12000"
    )
    private BaseGrpcGrpc.BaseGrpcStub stub;


    public void send()
    {

    }


    @Override
    public Future<Object> call(IGRPCClientRequest clientRequest)
    {
        Envelope.newBuilder().setPayload()
        GrpcRequest.newBuilder().setEnvelope()
        return null;
    }
}
