package com.cell.base.core.serialize;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:30
 */

public interface IOutputArchive
{
    void writeByte(byte b) throws IOException;

    void writeByteArray(Collection<Byte> arr) throws IOException;

    void writeBoolean(boolean b) throws IOException;

    void writeBooleanArray(Collection<Boolean> arr) throws IOException;

    void writeShort(short s) throws IOException;

    void writeShortArray(Collection<Short> arr) throws IOException;

    void writeInteger(int i) throws IOException;

    void writeIntegerArray(Collection<Integer> arr) throws IOException;

    void writeLong(long l) throws IOException;

    void writeLongArray(Collection<Long> arr) throws IOException;

    void writeFloat(float f) throws IOException;

    void writeFloatArray(Collection<Float> arr) throws IOException;

    void writeDouble(double d) throws IOException;

    void writeDoubleArray(Collection<Double> arr) throws IOException;

    void writeString(String s) throws IOException;

    void writeStringArray(Collection<String> arr) throws IOException;

    void writeBuffer(byte buf[]) throws IOException;

    byte[] toBytes() throws IOException;

    void writeShortLE(short s) throws IOException;

    void writeIntegerLE(int s) throws IOException;

    void writeLongLE(long s) throws IOException;

    void writeStringMap(Map<String, String> map) throws IOException;

    void writeRawBuffer(byte[] buf) throws IOException;

    void writeByte(String tag, byte b) throws IOException;

    void writeByteArray(String tag, Collection<Byte> arr) throws IOException;

    void writeBoolean(String tag, boolean b) throws IOException;

    void writeBooleanArray(String tag, Collection<Boolean> arr) throws IOException;

    void writeShort(String tag, short s) throws IOException;

    void writeShortArray(String tag, Collection<Short> arr) throws IOException;

    void writeInteger(String tag, int i) throws IOException;

    void writeIntegerArray(String tag, Collection<Integer> arr) throws IOException;

    void writeLong(String tag, long l) throws IOException;

    void writeLongArray(String tag, Collection<Long> arr) throws IOException;

    void writeFloat(String tag, float f) throws IOException;

    void writeFloatArray(String tag, Collection<Float> arr) throws IOException;

    void writeDouble(String tag, double d) throws IOException;

    void writeDoubleArray(String tag, Collection<Double> arr) throws IOException;

    void writeString(String tag, String s) throws IOException;

    void writeStringArray(String tag, Collection<String> arr) throws IOException;

    void writeBuffer(String tag, byte buf[]) throws IOException;

    <T extends ISerializable> void writeObjectArray(String tag, List<T> arr) throws IOException;

    IOutputArchive subArchive(String tag) throws IOException;

    void writeShortLE(String tag, short s) throws IOException;

    void writeIntegerLE(String tag, int s) throws IOException;

    void writeLongLE(String tag, long s) throws IOException;

    void writeStringMap(String tag, Map<String, String> map) throws IOException;

    <T extends ISerializable> void writeObjectMap(String tag, Map<String, T> map) throws IOException;

    void writeRawBuffer(String tag, byte[] buf) throws IOException;

    default void writeStringNullable(String s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeString(s);
        }
    }

    default void writeIntNullable(Integer s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeInteger(s);
        }
    }

    default void writeShortNullable(Short s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeShort(s);
        }
    }

    default void writeLongNullable(Long s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeLong(s);
        }
    }

    default void writeFloatNullable(Float s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeFloat(s);
        }
    }

    default void writeDoubleNullable(Double s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeDouble(s);
        }
    }

    default void writeBealeanNullable(Boolean s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeBoolean(s);
        }
    }

    default void writeByteNullable(Byte s) throws IOException
    {
        if (writeNullFlag(s))
        {
            writeByte(s);
        }
    }

    default boolean writeNullFlag(Object obj) throws IOException
    {
        if (obj == null)
        {
            writeByte((byte) 0);
            return false;
        } else
        {
            writeByte((byte) 1);
            return true;
        }
    }

    void writeByteNullable(String tag, Byte val) throws IOException;

    void writeIntegerNullable(String tag, Integer val) throws IOException;

    void writeShortNullable(String tag, Short val) throws IOException;

    void writeLongNullable(String tag, Long val) throws IOException;

    void writeDoubleNullable(String tag, Double val) throws IOException;

    void writeFloatNullable(String tag, Float val) throws IOException;

    void writeBooleanNullable(String tag, Boolean val) throws IOException;

    void writeStringNullable(String tag, String val) throws IOException;
}

