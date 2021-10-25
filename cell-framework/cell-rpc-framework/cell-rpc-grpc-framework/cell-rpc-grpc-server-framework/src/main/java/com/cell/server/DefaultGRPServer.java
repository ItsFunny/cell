package com.cell.server;

import com.cell.annotations.ActivePlugin;
import com.cell.concurrent.base.Promise;
import com.cell.config.GRPCServerConfiguration;
import com.cell.configuration.RootConfiguration;
import com.cell.constants.GRPCConstants;
import com.cell.context.InitCTX;
import com.cell.couple.GRPCServerResponse;
import com.cell.couple.RPCServerRequest;
import com.cell.exceptions.ProgramaException;
import com.cell.extension.GRPCServerExtension;
import com.cell.grpc.cluster.BaseGrpcGrpc;
import com.cell.grpc.cluster.GrpcRequest;
import com.cell.grpc.cluster.GrpcResponse;
import com.cell.grpc.common.Envelope;
import com.cell.grpc.common.EnvelopeHeader;
import com.cell.grpc.common.Payload;
import com.cell.log.LOG;
import com.cell.protocol.DefaultStringCommandProtocolID;
import com.cell.proxy.IRPCServerProxy;
import com.cell.utils.GRPCUtils;
import com.google.common.net.InetAddresses;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.grpc.stub.StreamObserver;
import io.netty.channel.unix.DomainSocketAddress;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
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
    private Server server = null;

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
                LOG.error("asd", e);
                responseObserver.onError(e);
                responseObserver.onCompleted();
            }
        }
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        super.onInit(ctx);

        Optional<Object> cfg = RootConfiguration.getInstance().getExtensionConfiguration(GRPCServerExtension.class);
        boolean present = cfg.isPresent();
        if (!present)
        {
            throw new ProgramaException("asd");
        }
        Object o = cfg.get();
        GRPCServerConfiguration configuration = (GRPCServerConfiguration) o;
//        DOMAIN_SOCKET_ADDRESS_PREFIX
        String address = configuration.getAddress();
        int port = configuration.getPort();
        NettyServerBuilder nettyServerBuilder = null;
        if (address.startsWith(GRPCConstants.DOMAIN_SOCKET_ADDRESS_PREFIX))
        {
            final String path = GRPCUtils.extractDomainSocketAddressPath(address);
            nettyServerBuilder = NettyServerBuilder.forAddress(new DomainSocketAddress(path))
                    .channelType(EpollServerDomainSocketChannel.class)
                    .bossEventLoopGroup(new io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup(1))
                    .workerEventLoopGroup(new EpollEventLoopGroup());
        } else if (GRPCConstants.ANY_IP_ADDRESS.equals(address))
        {
            nettyServerBuilder = NettyServerBuilder.forPort(port);
        } else
        {
            nettyServerBuilder = NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(address), port));
        }
        this.configure(nettyServerBuilder);
    }

    private void configure(NettyServerBuilder nettyServerBuilder)
    {

    }
    protected void configureServices(final ServerBuilder builder) {
        final Set<String> serviceNames = new LinkedHashSet<>();

        for (final GrpcServiceDefinition service : this.serviceList) {
            final String serviceName = service.getDefinition().getServiceDescriptor().getName();
            if (!serviceNames.add(serviceName)) {
                throw new IllegalStateException("Found duplicate service implementation: " + serviceName);
            }
            log.info("Registered gRPC service: " + serviceName + ", bean: " + service.getBeanName() + ", class: "
                    + service.getBeanClazz().getName());
            builder.addService(service.getDefinition());
        }
    }
}
