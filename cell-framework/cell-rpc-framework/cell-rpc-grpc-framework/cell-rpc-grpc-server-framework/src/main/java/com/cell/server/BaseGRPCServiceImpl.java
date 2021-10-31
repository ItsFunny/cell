package com.cell.server;

import com.cell.annotation.GRPCService;
import com.cell.annotations.AutoPlugin;
import com.cell.concurrent.base.Promise;
import com.cell.constants.ContextConstants;
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
import com.cell.serialize.ISerializable;
import com.cell.utils.JSONUtil;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 22:31
 */
@GRPCService
public class BaseGRPCServiceImpl extends BaseGrpcGrpc.BaseGrpcImplBase
{
    @AutoPlugin
    private IGRPCServer rpcServer;

    // TODO add default serialize

    @Override
    public void sendRequest(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver)
    {
        Envelope envelope = request.getEnvelope();
        EnvelopeHeader header = envelope.getHeader();
        RPCServerRequest rpcServerRequest = new RPCServerRequest();
        rpcServerRequest.setRequestSize((int) header.getLength());
        Payload payload = envelope.getPayload();
        InputStream inputStream = new ByteArrayInputStream(payload.getData().toByteArray());
        rpcServerRequest.setRequestStream(inputStream);
        DefaultStringCommandProtocolID protocolID = new DefaultStringCommandProtocolID(header.getProtocol());
        rpcServerRequest.setProtocolId(protocolID);

        GRPCServerResponse grpcServerResponse = new GRPCServerResponse();

        this.rpcServer.serve(rpcServerRequest, grpcServerResponse);
        Promise<Object> promise = grpcServerResponse.getPromise();

        try
        {
            Object o = promise.get();

            byte[] bytes = null;
            if (o instanceof ISerializable)
            {
                bytes = ((ISerializable) o).toBytes();
            } else
            {
                bytes = JSONUtil.toJsonString(o).getBytes();
            }
            // TODO ,返回值序列化成字节流
            GrpcResponse resp = GrpcResponse.newBuilder()
                    .setCode(ContextConstants.SUCCESS)
                    .setData(ByteString.copyFrom(bytes)).build();
            responseObserver.onNext(resp);
        } catch (Exception e)
        {
            LOG.error("asd", e);
            responseObserver.onError(e);
        } finally
        {
            responseObserver.onCompleted();
        }
    }
}
