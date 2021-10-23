package com.cell.server;

import com.cell.annotations.ActivePlugin;
import com.cell.concurrent.base.Promise;
import com.cell.couple.GRPCServerResponse;
import com.cell.couple.RPCServerRequest;
import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
import com.cell.log.LOG;
import com.cell.protocol.DefaultStringCommandProtocolID;
import com.cell.proxy.IRPCServerProxy;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:30
 */
@ActivePlugin
public class DefaultGRPServer extends BaseRPCServer implements IGRPCServer
{

    public DefaultGRPServer(IRPCServerProxy proxy)
    {
        super(proxy);
    }

    private BaseGRPCServiceImpl impl;

    class BaseGRPCServiceImpl extends BaseGrpcGrpc.BaseGrpcImplBase
    {
        @Override
        public void sendRequest(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver)
        {
            Envelope envelope = request.getEnvelope();
            EnvelopeHeader header = envelope.getHeader();
            RPCServerRequest rpcServerRequest = new RPCServerRequest();
            rpcServerRequest.setRequestSize((int) header.getLength());
            Payload payload = null;
            try
            {
                payload = Payload.parseFrom(envelope.getPayload());
            } catch (InvalidProtocolBufferException e)
            {
                responseObserver.onError(e);
                return;
            }
            InputStream inputStream = new ByteArrayInputStream(payload.getData().toByteArray());
            rpcServerRequest.setRequestStream(inputStream);
            DefaultStringCommandProtocolID protocolID = new DefaultStringCommandProtocolID(header.getProtocol());
            rpcServerRequest.setProtocolId(protocolID);


            GRPCServerResponse grpcServerResponse = new GRPCServerResponse();

            DefaultGRPServer.this.serve(rpcServerRequest, grpcServerResponse);
            Promise<Object> promise = grpcServerResponse.getPromise();

            try
            {
                promise.get(30, TimeUnit.SECONDS);
            } catch (Exception e)
            {
                LOG.error("asd",e);
                responseObserver.onError(e);
                responseObserver.onCompleted();
            }
        }
    }

}
