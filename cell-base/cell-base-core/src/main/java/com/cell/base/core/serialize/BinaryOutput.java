package com.cell.base.core.serialize;


import com.cell.base.common.utils.NetworkUtil;
import com.cell.base.common.utils.StringUtils;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 22:20
 */

public class BinaryOutput implements IOutputArchive
{
    private DataOutput out;
    private ByteArrayOutputStream stream;

    public static BinaryOutput createArchive()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        return new BinaryOutput(stream);
    }

    @Deprecated
    public static BinaryOutput createArchive(OutputStream out)
    {
        return new BinaryOutput(new DataOutputStream(out));
    }

    private BinaryOutput(DataOutput out)
    {
        this.out = out;
    }

    public BinaryOutput(ByteArrayOutputStream stream)
    {
        this.stream = stream;
        out = new DataOutputStream(stream);
    }

    @Override
    public byte[] toBytes()
    {
        return stream.toByteArray();
    }

    @Override
    public void writeByte(byte b) throws IOException
    {
        out.writeByte(b);
    }

    @Override
    public void writeByteArray(Collection<Byte> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Byte e : arr)
            {
                writeByte(e);
            }
        }
    }

    @Override
    public void writeBoolean(boolean b) throws IOException
    {
        out.writeBoolean(b);
    }

    @Override
    public void writeBooleanArray(Collection<Boolean> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Boolean e : arr)
            {
                writeBoolean(e);
            }
        }
    }

    @Override
    public void writeShortLE(short s) throws IOException
    {
        out.writeShort(NetworkUtil.shortBE2LE(s));
    }

    @Override
    public void writeIntegerLE(int s) throws IOException
    {
        out.writeInt(NetworkUtil.intBE2LE(s));
    }

    @Override
    public void writeLongLE(long s) throws IOException
    {
        out.writeLong(NetworkUtil.longBE2LE(s));
    }

    @Override
    public void writeShort(short s) throws IOException
    {
        out.writeShort(s);
    }

    @Override
    public void writeShortArray(Collection<Short> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Short e : arr)
            {
                writeShort(e);
            }
        }
    }

    @Override
    public void writeInteger(int i) throws IOException
    {
        out.writeInt(i);
    }

    @Override
    public void writeIntegerArray(Collection<Integer> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Integer e : arr)
            {
                writeInteger(e);
            }
        }
    }

    @Override
    public void writeLong(long l) throws IOException
    {
        out.writeLong(l);
    }

    @Override
    public void writeLongArray(Collection<Long> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Long e : arr)
            {
                writeLong(e);
            }
        }
    }

    @Override
    public void writeFloat(float f) throws IOException
    {
        out.writeFloat(f);
    }

    @Override
    public void writeFloatArray(Collection<Float> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Float e : arr)
            {
                writeFloat(e);
            }
        }
    }

    @Override
    public void writeDouble(double d) throws IOException
    {
        out.writeDouble(d);
    }

    @Override
    public void writeDoubleArray(Collection<Double> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (Double e : arr)
            {
                writeDouble(e);
            }
        }
    }

    @Override
    public void writeString(String s) throws IOException
    {
        byte[] bytes = StringUtils.toBytes(s);
        writeInteger(bytes.length);
        out.write(bytes);
    }


    @Override
    public void writeStringArray(Collection<String> arr) throws IOException
    {
        writeInteger(arr != null ? arr.size() : 0);
        if (arr != null && arr.size() > 0)
        {
            for (String e : arr)
            {
                writeString(e);
            }
        }
    }

    @Override
    public void writeStringMap(Map<String, String> map) throws IOException
    {
        writeInteger(map.size());
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            writeString(entry.getKey());
            writeString(entry.getValue());
        }
    }

    @Override
    public void writeBuffer(byte[] buf) throws IOException
    {
        writeInteger(buf.length);
        out.write(buf);
    }

    @Override
    public void writeRawBuffer(byte[] buf) throws IOException
    {
        out.write(buf);
    }

    @Override
    public void writeByte(String tag, byte b) throws IOException
    {
        writeByte(b);
    }

    @Override
    public void writeByteArray(String tag, Collection<Byte> arr) throws IOException
    {
        writeByteArray(arr);
    }

    @Override
    public void writeBoolean(String tag, boolean b) throws IOException
    {
        writeBoolean(b);
    }

    @Override
    public void writeBooleanArray(String tag, Collection<Boolean> arr) throws IOException
    {
        writeBooleanArray(arr);
    }

    @Override
    public void writeShort(String tag, short s) throws IOException
    {
        writeShort(s);
    }

    @Override
    public void writeShortArray(String tag, Collection<Short> arr) throws IOException
    {
        writeShortArray(arr);
    }

    @Override
    public void writeInteger(String tag, int i) throws IOException
    {
        writeInteger(i);
    }

    @Override
    public void writeIntegerArray(String tag, Collection<Integer> arr) throws IOException
    {
        writeIntegerArray(arr);
    }

    @Override
    public void writeLong(String tag, long l) throws IOException
    {
        writeLong(l);
    }

    @Override
    public void writeLongArray(String tag, Collection<Long> arr) throws IOException
    {
        writeLongArray(arr);
    }

    @Override
    public void writeFloat(String tag, float f) throws IOException
    {
        writeFloat(f);
    }

    @Override
    public void writeFloatArray(String tag, Collection<Float> arr) throws IOException
    {
        writeFloatArray(arr);
    }

    @Override
    public void writeDouble(String tag, double d) throws IOException
    {
        writeDouble(d);
    }

    @Override
    public void writeDoubleArray(String tag, Collection<Double> arr) throws IOException
    {
        writeDoubleArray(arr);
    }

    @Override
    public void writeString(String tag, String s) throws IOException
    {
        writeString(s);
    }

    @Override
    public void writeStringArray(String tag, Collection<String> arr) throws IOException
    {
        writeStringArray(arr);
    }

    @Override
    public void writeBuffer(String tag, byte[] buf) throws IOException
    {
        writeBuffer(buf);
    }

    @Override
    public <T extends ISerializable> void writeObjectArray(String tag, List<T> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeShortLE(String tag, short s) throws IOException
    {
        writeShortLE(s);
    }

    @Override
    public void writeIntegerLE(String tag, int s) throws IOException
    {
        writeIntegerLE(s);
    }

    @Override
    public void writeLongLE(String tag, long s) throws IOException
    {
        writeLongLE(s);
    }

    @Override
    public void writeStringMap(String tag, Map<String, String> map) throws IOException
    {
        writeStringMap(map);
    }

    @Override
    public <T extends ISerializable> void writeObjectMap(String tag, Map<String, T> map) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeRawBuffer(String tag, byte[] buf) throws IOException
    {
        writeRawBuffer(buf);
    }

    @Override
    public void writeByteNullable(String tag, Byte val) throws IOException
    {
        writeByteNullable(val);
    }

    @Override
    public void writeIntegerNullable(String tag, Integer val) throws IOException
    {
        writeIntNullable(val);
    }

    @Override
    public void writeShortNullable(String tag, Short val) throws IOException
    {
        writeShortNullable(val);
    }

    @Override
    public void writeLongNullable(String tag, Long val) throws IOException
    {
        writeLongNullable(val);
    }

    @Override
    public void writeDoubleNullable(String tag, Double val) throws IOException
    {
        writeDoubleNullable(val);
    }

    @Override
    public void writeFloatNullable(String tag, Float val) throws IOException
    {
        writeFloatNullable(val);
    }

    @Override
    public void writeBooleanNullable(String tag, Boolean val) throws IOException
    {
        writeBealeanNullable(val);
    }

    @Override
    public void writeStringNullable(String tag, String val) throws IOException
    {
        writeStringNullable(val);
    }

    @Override
    public IOutputArchive subArchive(String tag) throws IOException
    {
        return this;
    }

    private void throwNotSupportOperation()
    {
        throw new RuntimeException("BinaryOutput not support operation");
    }
}
