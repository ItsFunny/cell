package com.cell.grpc.command;

import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.log.LOG;
import com.cell.models.Module;
import io.grpc.stub.StreamObserver;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 13:34
 */
public abstract class BaseGRPCServer extends BaseGrpcGrpc.BaseGrpcImplBase
{
    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void sendRequest(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver)
    {
        super.sendRequest(request, responseObserver);
    }

    private void parseRequest(GrpcRequest request)
    {
        LOG.info(Module.RPC, "解析request,获取command");
    }

    protected abstract void onExecute();
}
