// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common/envelope.proto

package com.cell.grpc.common;

public interface EnvelopeHeaderOrBuilder extends
        // @@protoc_insertion_point(interface_extends:common.EnvelopeHeader)
        com.google.protobuf.MessageOrBuilder
{

    /**
     * <pre>
     * 0位代表的是大小端
     * </pre>
     *
     * <code>int64 flag = 1;</code>
     *
     * @return The flag.
     */
    long getFlag();

    /**
     * <code>int64 length = 2;</code>
     *
     * @return The length.
     */
    long getLength();

    /**
     * <code>string protocol = 3;</code>
     *
     * @return The protocol.
     */
    String getProtocol();

    /**
     * <code>string protocol = 3;</code>
     *
     * @return The bytes for protocol.
     */
    com.google.protobuf.ByteString
    getProtocolBytes();

    /**
     * <code>string sequenceId = 4;</code>
     *
     * @return The sequenceId.
     */
    String getSequenceId();

    /**
     * <code>string sequenceId = 4;</code>
     *
     * @return The bytes for sequenceId.
     */
    com.google.protobuf.ByteString
    getSequenceIdBytes();
}
