package com.cell.server;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.couple.RPCServerRequest;
import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
import com.cell.protocol.DefaultStringCommandProtocolID;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:31
 */
@ActivePlugin
public class BaseGRPCServiceImpl extends BaseGrpcGrpc.BaseGrpcImplBase
{
    @AutoPlugin
    private IRPCServer rpcServer;




}
