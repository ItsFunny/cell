// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common/envelope.proto

package com.cell.grpc.common;

public interface EnvelopeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:common.Envelope)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.common.EnvelopeHeader header = 1;</code>
   * @return Whether the header field is set.
   */
  boolean hasHeader();
  /**
   * <code>.common.EnvelopeHeader header = 1;</code>
   * @return The header.
   */
  EnvelopeHeader getHeader();
  /**
   * <code>.common.EnvelopeHeader header = 1;</code>
   */
  EnvelopeHeaderOrBuilder getHeaderOrBuilder();

  /**
   * <code>.common.Payload payload = 2;</code>
   * @return Whether the payload field is set.
   */
  boolean hasPayload();
  /**
   * <code>.common.Payload payload = 2;</code>
   * @return The payload.
   */
  Payload getPayload();
  /**
   * <code>.common.Payload payload = 2;</code>
   */
  PayloadOrBuilder getPayloadOrBuilder();
}
