package com.cell.grpc.cluster;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.29.0)",
    comments = "Source: cluster/cluster.proto")
public final class BaseGrpcGrpc {

  private BaseGrpcGrpc() {}

  public static final String SERVICE_NAME = "BaseGrpc";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.cell.grpc.cluster.GrpcRequest,
      com.cell.grpc.cluster.GrpcResponse> getSendRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendRequest",
      requestType = com.cell.grpc.cluster.GrpcRequest.class,
      responseType = com.cell.grpc.cluster.GrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.cell.grpc.cluster.GrpcRequest,
      com.cell.grpc.cluster.GrpcResponse> getSendRequestMethod() {
    io.grpc.MethodDescriptor<com.cell.grpc.cluster.GrpcRequest, com.cell.grpc.cluster.GrpcResponse> getSendRequestMethod;
    if ((getSendRequestMethod = BaseGrpcGrpc.getSendRequestMethod) == null) {
      synchronized (BaseGrpcGrpc.class) {
        if ((getSendRequestMethod = BaseGrpcGrpc.getSendRequestMethod) == null) {
          BaseGrpcGrpc.getSendRequestMethod = getSendRequestMethod =
              io.grpc.MethodDescriptor.<com.cell.grpc.cluster.GrpcRequest, com.cell.grpc.cluster.GrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cell.grpc.cluster.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cell.grpc.cluster.GrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BaseGrpcMethodDescriptorSupplier("sendRequest"))
              .build();
        }
      }
    }
    return getSendRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BaseGrpcStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseGrpcStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseGrpcStub>() {
        @Override
        public BaseGrpcStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseGrpcStub(channel, callOptions);
        }
      };
    return BaseGrpcStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BaseGrpcBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseGrpcBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseGrpcBlockingStub>() {
        @Override
        public BaseGrpcBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseGrpcBlockingStub(channel, callOptions);
        }
      };
    return BaseGrpcBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BaseGrpcFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BaseGrpcFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BaseGrpcFutureStub>() {
        @Override
        public BaseGrpcFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BaseGrpcFutureStub(channel, callOptions);
        }
      };
    return BaseGrpcFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class BaseGrpcImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendRequest(com.cell.grpc.cluster.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.cell.grpc.cluster.GrpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSendRequestMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cell.grpc.cluster.GrpcRequest,
                com.cell.grpc.cluster.GrpcResponse>(
                  this, METHODID_SEND_REQUEST)))
          .build();
    }
  }

  /**
   */
  public static final class BaseGrpcStub extends io.grpc.stub.AbstractAsyncStub<BaseGrpcStub> {
    private BaseGrpcStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected BaseGrpcStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseGrpcStub(channel, callOptions);
    }

    /**
     */
    public void sendRequest(com.cell.grpc.cluster.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.cell.grpc.cluster.GrpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendRequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BaseGrpcBlockingStub extends io.grpc.stub.AbstractBlockingStub<BaseGrpcBlockingStub> {
    private BaseGrpcBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected BaseGrpcBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseGrpcBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.cell.grpc.cluster.GrpcResponse sendRequest(com.cell.grpc.cluster.GrpcRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BaseGrpcFutureStub extends io.grpc.stub.AbstractFutureStub<BaseGrpcFutureStub> {
    private BaseGrpcFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected BaseGrpcFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BaseGrpcFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cell.grpc.cluster.GrpcResponse> sendRequest(
        com.cell.grpc.cluster.GrpcRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_REQUEST = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BaseGrpcImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BaseGrpcImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_REQUEST:
          serviceImpl.sendRequest((com.cell.grpc.cluster.GrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.cell.grpc.cluster.GrpcResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BaseGrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BaseGrpcBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cell.grpc.cluster.ClusterProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BaseGrpc");
    }
  }

  private static final class BaseGrpcFileDescriptorSupplier
      extends BaseGrpcBaseDescriptorSupplier {
    BaseGrpcFileDescriptorSupplier() {}
  }

  private static final class BaseGrpcMethodDescriptorSupplier
      extends BaseGrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BaseGrpcMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BaseGrpcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BaseGrpcFileDescriptorSupplier())
              .addMethod(getSendRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
