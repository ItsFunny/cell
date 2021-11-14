package com.cell.rpc.grpc.client.interceptor;

import com.cell.base.common.models.Module;
import com.cell.sdk.log.LOG;
import io.grpc.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-14 16:07
 */
public class LogInterceptor implements ClientInterceptor
{
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            final MethodDescriptor<ReqT, RespT> method,
            final CallOptions callOptions,
            final Channel next)
    {

        LOG.info(Module.GRPC_CLIENT, "Received call to {}", method.getFullMethodName());
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions))
        {

            @Override
            public void sendMessage(ReqT message)
            {
                LOG.debug(Module.GRPC_CLIENT, "Request message: {}", message);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers)
            {
                super.start(
                        new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener)
                        {
                            @Override
                            public void onMessage(RespT message)
                            {
                                LOG.info(Module.GRPC_CLIENT, "Response message: {}", message);
                                super.onMessage(message);
                            }

                            @Override
                            public void onHeaders(Metadata headers)
                            {
                                LOG.debug(Module.GRPC_CLIENT, "gRPC headers: {}", headers);
                                super.onHeaders(headers);
                            }

                            @Override
                            public void onClose(Status status, Metadata trailers)
                            {
                                LOG.info(Module.GRPC_CLIENT, "Interaction ends with status: {}", status);
                                LOG.info(Module.GRPC_CLIENT, "Trailers: {}", trailers);
                                super.onClose(status, trailers);
                            }
                        }, headers);
            }
        };
    }
}
