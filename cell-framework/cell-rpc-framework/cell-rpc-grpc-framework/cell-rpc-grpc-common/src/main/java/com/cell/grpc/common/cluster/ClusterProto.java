// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: cluster/cluster.proto

package com.cell.grpc.common.cluster;

import com.cell.grpc.common.EvelopeProto;

public final class ClusterProto
{
    private ClusterProto() {}

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistryLite registry)
    {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry)
    {
        registerAllExtensions(
                (com.google.protobuf.ExtensionRegistryLite) registry);
    }

    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_GrpcRequest_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_GrpcRequest_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_GrpcResponse_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_GrpcResponse_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor()
    {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static
    {
        String[] descriptorData = {
                "\n\025cluster/cluster.proto\032\025common/envelope" +
                        ".proto\"1\n\013GrpcRequest\022\"\n\010envelope\030\001 \001(\0132" +
                        "\020.common.Envelope\";\n\014GrpcResponse\022\017\n\007mes" +
                        "sage\030\001 \001(\t\022\014\n\004code\030\002 \001(\003\022\014\n\004data\030\003 \001(\01428" +
                        "\n\010BaseGrpc\022,\n\013sendRequest\022\014.GrpcRequest\032" +
                        "\r.GrpcResponse\"\000B\'\n\025com.cell.grpc.cluste" +
                        "rB\014ClusterProtoP\001b\006proto3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                                EvelopeProto.getDescriptor(),
                        });
        internal_static_GrpcRequest_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_GrpcRequest_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_GrpcRequest_descriptor,
                new String[]{"Envelope",});
        internal_static_GrpcResponse_descriptor =
                getDescriptor().getMessageTypes().get(1);
        internal_static_GrpcResponse_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_GrpcResponse_descriptor,
                new String[]{"Message", "Code", "Data",});
        EvelopeProto.getDescriptor();
    }

    // @@protoc_insertion_point(outer_class_scope)
}
