package com.cell.serialize;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:30
 */

public interface IInputArchive
{
    byte readByte() throws IOException;

    List<Byte> readByteArray() throws IOException;

    boolean readBoolean() throws IOException;

    List<Boolean> readBooleanArray() throws IOException;

    short readShort() throws IOException;

    List<Short> readShortArray() throws IOException;

    int readInteger() throws IOException;

    List<Integer> readIntegerArray() throws IOException;

    long readLong() throws IOException;

    List<Long> readLongArray() throws IOException;

    float readFloat() throws IOException;

    List<Float> readFloatArray() throws IOException;

    double readDouble() throws IOException;

    List<Double> readDoubleArray() throws IOException;

    String readString() throws IOException;

    List<String> readStringArray() throws IOException;

    void readStringArray(Collection<String> arr) throws IOException;

    byte[] readBuffer() throws IOException;

    int skipBytes(int n) throws IOException;

    long readLongLE() throws IOException;

    int readIntegerLE() throws IOException;

    short readShortLE() throws IOException;

    Map<String, String> readStringMap() throws IOException;

    byte[] readRawBuffer(int len) throws IOException;

    IInputArchive readSubArchive(String tag) throws IOException;

    byte readByte(String tag) throws IOException;

    List<Byte> readByteArray(String tag) throws IOException;

    boolean readBoolean(String tag) throws IOException;

    List<Boolean> readBooleanArray(String tag) throws IOException;

    short readShort(String tag) throws IOException;

    List<Short> readShortArray(String tag) throws IOException;

    int readInteger(String tag) throws IOException;

    List<Integer> readIntegerArray(String tag) throws IOException;

    TreeSet<Integer> readIntegerTreeSet(String tag) throws IOException;

    long readLong(String tag) throws IOException;

    List<Long> readLongArray(String tag) throws IOException;

    float readFloat(String tag) throws IOException;

    List<Float> readFloatArray(String tag) throws IOException;

    double readDouble(String tag) throws IOException;

    List<Double> readDoubleArray(String tag) throws IOException;

    String readString(String tag) throws IOException;

    List<String> readStringArray(String tag) throws IOException;

    void readStringArray(String tag, Collection<String> arr) throws IOException;

    byte[] readBuffer(String tag) throws IOException;

    long readLongLE(String tag) throws IOException;

    int readIntegerLE(String tag) throws IOException;

    short readShortLE(String tag) throws IOException;

    Map<String, String> readStringMap(String tag) throws IOException;

    <T extends ISerializable> Map<String, T> readObjectMap(String tag, Class<T> clazz) throws IOException;

    byte[] readRawBuffer(String tag, int len) throws IOException;

    <T extends ISerializable> T readObjectNullable(String tag, Class<T> clazz) throws IOException;

    <T extends ISerializable> List<T> readObjectArray(String tag, Class<T> clazz) throws IOException;

    default boolean readNullFlag() throws IOException
    {
        byte flag = readByte();
        if (flag == 1)
        {
            return true;
        }
        return false;
    }

    default Byte readByteNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readByte();
        }
        return null;
    }

    default Integer readIntNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readInteger();
        }
        return null;
    }

    default Short readShortNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readShort();
        }
        return null;
    }

    default Long readLongNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readLong();
        }
        return null;
    }

    default Double readDoubleNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readDouble();
        }
        return null;
    }

    default Float readFloatNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readFloat();
        }
        return null;
    }

    default Boolean readBooleanNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readBoolean();
        }
        return null;
    }

    default String readStringNullable() throws IOException
    {
        if (readNullFlag())
        {
            return readString();
        }
        return null;
    }

    Byte readByteNullable(String tag) throws IOException;

    Integer readIntegerNullable(String tag) throws IOException;

    Short readShortNullable(String tag) throws IOException;

    Long readLongNullable(String tag) throws IOException;

    Double readDoubleNullable(String tag) throws IOException;

    Float readFloatNullable(String tag) throws IOException;

    Boolean readBooleanNullable(String tag) throws IOException;

    String readStringNullable(String tag) throws IOException;
}

