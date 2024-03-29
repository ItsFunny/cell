// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common/envelope.proto

package com.cell.grpc.common;

/**
 * Protobuf type {@code common.Payload}
 */
public final class Payload extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:common.Payload)
        PayloadOrBuilder
{
    private static final long serialVersionUID = 0L;

    // Use Payload.newBuilder() to construct.
    private Payload(com.google.protobuf.GeneratedMessageV3.Builder<?> builder)
    {
        super(builder);
    }

    private Payload()
    {
        data_ = com.google.protobuf.ByteString.EMPTY;
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
            UnusedPrivateParameter unused)
    {
        return new Payload();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields()
    {
        return this.unknownFields;
    }

    private Payload(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        this();
        if (extensionRegistry == null)
        {
            throw new NullPointerException();
        }
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
        try
        {
            boolean done = false;
            while (!done)
            {
                int tag = input.readTag();
                switch (tag)
                {
                    case 0:
                        done = true;
                        break;
                    case 10:
                    {
                        Header.Builder subBuilder = null;
                        if (header_ != null)
                        {
                            subBuilder = header_.toBuilder();
                        }
                        header_ = input.readMessage(Header.parser(), extensionRegistry);
                        if (subBuilder != null)
                        {
                            subBuilder.mergeFrom(header_);
                            header_ = subBuilder.buildPartial();
                        }

                        break;
                    }
                    case 18:
                    {

                        data_ = input.readBytes();
                        break;
                    }
                    default:
                    {
                        if (!parseUnknownField(
                                input, unknownFields, extensionRegistry, tag))
                        {
                            done = true;
                        }
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e)
        {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e)
        {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                    e).setUnfinishedMessage(this);
        } finally
        {
            this.unknownFields = unknownFields.build();
            makeExtensionsImmutable();
        }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor()
    {
        return EvelopeProto.internal_static_common_Payload_descriptor;
    }

    @Override
    protected FieldAccessorTable
    internalGetFieldAccessorTable()
    {
        return EvelopeProto.internal_static_common_Payload_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        Payload.class, Payload.Builder.class);
    }

    public static final int HEADER_FIELD_NUMBER = 1;
    private Header header_;

    /**
     * <code>.common.Header header = 1;</code>
     *
     * @return Whether the header field is set.
     */
    public boolean hasHeader()
    {
        return header_ != null;
    }

    /**
     * <code>.common.Header header = 1;</code>
     *
     * @return The header.
     */
    public Header getHeader()
    {
        return header_ == null ? Header.getDefaultInstance() : header_;
    }

    /**
     * <code>.common.Header header = 1;</code>
     */
    public HeaderOrBuilder getHeaderOrBuilder()
    {
        return getHeader();
    }

    public static final int DATA_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString data_;

    /**
     * <code>bytes data = 2;</code>
     *
     * @return The data.
     */
    public com.google.protobuf.ByteString getData()
    {
        return data_;
    }

    private byte memoizedIsInitialized = -1;

    @Override
    public final boolean isInitialized()
    {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) return true;
        if (isInitialized == 0) return false;

        memoizedIsInitialized = 1;
        return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException
    {
        if (header_ != null)
        {
            output.writeMessage(1, getHeader());
        }
        if (!data_.isEmpty())
        {
            output.writeBytes(2, data_);
        }
        unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize()
    {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (header_ != null)
        {
            size += com.google.protobuf.CodedOutputStream
                    .computeMessageSize(1, getHeader());
        }
        if (!data_.isEmpty())
        {
            size += com.google.protobuf.CodedOutputStream
                    .computeBytesSize(2, data_);
        }
        size += unknownFields.getSerializedSize();
        memoizedSize = size;
        return size;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Payload))
        {
            return super.equals(obj);
        }
        Payload other = (Payload) obj;

        if (hasHeader() != other.hasHeader()) return false;
        if (hasHeader())
        {
            if (!getHeader()
                    .equals(other.getHeader()))
            {
                return false;
            }
        }
        if (!getData()
                .equals(other.getData()))
        {
            return false;
        }
        if (!unknownFields.equals(other.unknownFields)) return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        if (memoizedHashCode != 0)
        {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        if (hasHeader())
        {
            hash = (37 * hash) + HEADER_FIELD_NUMBER;
            hash = (53 * hash) + getHeader().hashCode();
        }
        hash = (37 * hash) + DATA_FIELD_NUMBER;
        hash = (53 * hash) + getData().hashCode();
        hash = (29 * hash) + unknownFields.hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    public static Payload parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        return PARSER.parseFrom(data);
    }

    public static Payload parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Payload parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        return PARSER.parseFrom(data);
    }

    public static Payload parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Payload parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        return PARSER.parseFrom(data);
    }

    public static Payload parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException
    {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Payload parseFrom(java.io.InputStream input)
            throws java.io.IOException
    {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static Payload parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException
    {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Payload parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException
    {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static Payload parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException
    {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static Payload parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException
    {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static Payload parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException
    {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }

    public static Builder newBuilder()
    {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(Payload prototype)
    {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @Override
    public Builder toBuilder()
    {
        return this == DEFAULT_INSTANCE
                ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
            BuilderParent parent)
    {
        Builder builder = new Builder(parent);
        return builder;
    }

    /**
     * Protobuf type {@code common.Payload}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:common.Payload)
            PayloadOrBuilder
    {
        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor()
        {
            return EvelopeProto.internal_static_common_Payload_descriptor;
        }

        @Override
        protected FieldAccessorTable
        internalGetFieldAccessorTable()
        {
            return EvelopeProto.internal_static_common_Payload_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            Payload.class, Payload.Builder.class);
        }

        // Construct using com.cell.grpc.common.Payload.newBuilder()
        private Builder()
        {
            maybeForceBuilderInitialization();
        }

        private Builder(
                BuilderParent parent)
        {
            super(parent);
            maybeForceBuilderInitialization();
        }

        private void maybeForceBuilderInitialization()
        {
            if (com.google.protobuf.GeneratedMessageV3
                    .alwaysUseFieldBuilders)
            {
            }
        }

        @Override
        public Builder clear()
        {
            super.clear();
            if (headerBuilder_ == null)
            {
                header_ = null;
            } else
            {
                header_ = null;
                headerBuilder_ = null;
            }
            data_ = com.google.protobuf.ByteString.EMPTY;

            return this;
        }

        @Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType()
        {
            return EvelopeProto.internal_static_common_Payload_descriptor;
        }

        @Override
        public Payload getDefaultInstanceForType()
        {
            return Payload.getDefaultInstance();
        }

        @Override
        public Payload build()
        {
            Payload result = buildPartial();
            if (!result.isInitialized())
            {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @Override
        public Payload buildPartial()
        {
            Payload result = new Payload(this);
            if (headerBuilder_ == null)
            {
                result.header_ = header_;
            } else
            {
                result.header_ = headerBuilder_.build();
            }
            result.data_ = data_;
            onBuilt();
            return result;
        }

        @Override
        public Builder clone()
        {
            return super.clone();
        }

        @Override
        public Builder setField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value)
        {
            return super.setField(field, value);
        }

        @Override
        public Builder clearField(
                com.google.protobuf.Descriptors.FieldDescriptor field)
        {
            return super.clearField(field);
        }

        @Override
        public Builder clearOneof(
                com.google.protobuf.Descriptors.OneofDescriptor oneof)
        {
            return super.clearOneof(oneof);
        }

        @Override
        public Builder setRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                int index, Object value)
        {
            return super.setRepeatedField(field, index, value);
        }

        @Override
        public Builder addRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value)
        {
            return super.addRepeatedField(field, value);
        }

        @Override
        public Builder mergeFrom(com.google.protobuf.Message other)
        {
            if (other instanceof Payload)
            {
                return mergeFrom((Payload) other);
            } else
            {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(Payload other)
        {
            if (other == Payload.getDefaultInstance()) return this;
            if (other.hasHeader())
            {
                mergeHeader(other.getHeader());
            }
            if (other.getData() != com.google.protobuf.ByteString.EMPTY)
            {
                setData(other.getData());
            }
            this.mergeUnknownFields(other.unknownFields);
            onChanged();
            return this;
        }

        @Override
        public final boolean isInitialized()
        {
            return true;
        }

        @Override
        public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException
        {
            Payload parsedMessage = null;
            try
            {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e)
            {
                parsedMessage = (Payload) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally
            {
                if (parsedMessage != null)
                {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        private Header header_;
        private com.google.protobuf.SingleFieldBuilderV3<
                Header, Header.Builder, HeaderOrBuilder> headerBuilder_;

        /**
         * <code>.common.Header header = 1;</code>
         *
         * @return Whether the header field is set.
         */
        public boolean hasHeader()
        {
            return headerBuilder_ != null || header_ != null;
        }

        /**
         * <code>.common.Header header = 1;</code>
         *
         * @return The header.
         */
        public Header getHeader()
        {
            if (headerBuilder_ == null)
            {
                return header_ == null ? Header.getDefaultInstance() : header_;
            } else
            {
                return headerBuilder_.getMessage();
            }
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        public Builder setHeader(Header value)
        {
            if (headerBuilder_ == null)
            {
                if (value == null)
                {
                    throw new NullPointerException();
                }
                header_ = value;
                onChanged();
            } else
            {
                headerBuilder_.setMessage(value);
            }

            return this;
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        public Builder setHeader(
                Header.Builder builderForValue)
        {
            if (headerBuilder_ == null)
            {
                header_ = builderForValue.build();
                onChanged();
            } else
            {
                headerBuilder_.setMessage(builderForValue.build());
            }

            return this;
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        public Builder mergeHeader(Header value)
        {
            if (headerBuilder_ == null)
            {
                if (header_ != null)
                {
                    header_ =
                            Header.newBuilder(header_).mergeFrom(value).buildPartial();
                } else
                {
                    header_ = value;
                }
                onChanged();
            } else
            {
                headerBuilder_.mergeFrom(value);
            }

            return this;
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        public Builder clearHeader()
        {
            if (headerBuilder_ == null)
            {
                header_ = null;
                onChanged();
            } else
            {
                header_ = null;
                headerBuilder_ = null;
            }

            return this;
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        public Header.Builder getHeaderBuilder()
        {

            onChanged();
            return getHeaderFieldBuilder().getBuilder();
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        public HeaderOrBuilder getHeaderOrBuilder()
        {
            if (headerBuilder_ != null)
            {
                return headerBuilder_.getMessageOrBuilder();
            } else
            {
                return header_ == null ?
                        Header.getDefaultInstance() : header_;
            }
        }

        /**
         * <code>.common.Header header = 1;</code>
         */
        private com.google.protobuf.SingleFieldBuilderV3<
                Header, Header.Builder, HeaderOrBuilder>
        getHeaderFieldBuilder()
        {
            if (headerBuilder_ == null)
            {
                headerBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
                        Header, Header.Builder, HeaderOrBuilder>(
                        getHeader(),
                        getParentForChildren(),
                        isClean());
                header_ = null;
            }
            return headerBuilder_;
        }

        private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;

        /**
         * <code>bytes data = 2;</code>
         *
         * @return The data.
         */
        public com.google.protobuf.ByteString getData()
        {
            return data_;
        }

        /**
         * <code>bytes data = 2;</code>
         *
         * @param value The data to set.
         * @return This builder for chaining.
         */
        public Builder setData(com.google.protobuf.ByteString value)
        {
            if (value == null)
            {
                throw new NullPointerException();
            }

            data_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>bytes data = 2;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearData()
        {

            data_ = getDefaultInstance().getData();
            onChanged();
            return this;
        }

        @Override
        public final Builder setUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields)
        {
            return super.setUnknownFields(unknownFields);
        }

        @Override
        public final Builder mergeUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields)
        {
            return super.mergeUnknownFields(unknownFields);
        }


        // @@protoc_insertion_point(builder_scope:common.Payload)
    }

    // @@protoc_insertion_point(class_scope:common.Payload)
    private static final Payload DEFAULT_INSTANCE;

    static
    {
        DEFAULT_INSTANCE = new Payload();
    }

    public static Payload getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Payload>
            PARSER = new com.google.protobuf.AbstractParser<Payload>()
    {
        @Override
        public Payload parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException
        {
            return new Payload(input, extensionRegistry);
        }
    };

    public static com.google.protobuf.Parser<Payload> parser()
    {
        return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Payload> getParserForType()
    {
        return PARSER;
    }

    @Override
    public Payload getDefaultInstanceForType()
    {
        return DEFAULT_INSTANCE;
    }

}

