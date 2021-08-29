package com.cell.serialize;

import com.cell.json.PacketJSON;
import com.cell.utils.NetworkUtil;
import com.cell.utils.StringUtils;
import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:44
 */
public class JsonOutput implements IOutputArchive
{
    private final Map<String, Object> out = new HashMap<>();

    public static JsonOutput createArchive()
    {
        return new JsonOutput();
    }

    public JsonOutput()
    {
    }

    private Map<String, Object> getOutMap()
    {
        return out;
    }

    @Override
    public byte[] toBytes() throws IOException
    {
        String jsonString = PacketJSON.toJsonString(out);
        return StringUtils.toBytes(jsonString);
    }

    /********************************************************************************
     * json serialize
     ********************************************************************************/

    @Override
    public IOutputArchive subArchive(String tag) throws IOException
    {
        Object sub = out.get(tag);
        if (sub == null)
        {
            sub = JsonOutput.createArchive();
            out.put(tag, ((JsonOutput) sub).getOutMap());
        }
        return (IOutputArchive) sub;
    }

    @Override
    public void writeByte(String tag, byte b) throws IOException
    {
        out.put(tag, b);
    }

    @Override
    public void writeByteArray(String tag, Collection<Byte> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeBoolean(String tag, boolean b) throws IOException
    {
        out.put(tag, b);
    }

    @Override
    public void writeBooleanArray(String tag, Collection<Boolean> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeShort(String tag, short s) throws IOException
    {
        out.put(tag, s);
    }

    @Override
    public void writeShortArray(String tag, Collection<Short> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeInteger(String tag, int i) throws IOException
    {
        out.put(tag, i);
    }

    @Override
    public void writeIntegerArray(String tag, Collection<Integer> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeLong(String tag, long l) throws IOException
    {
        out.put(tag, l);
    }

    @Override
    public void writeLongArray(String tag, Collection<Long> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeFloat(String tag, float f) throws IOException
    {
        out.put(tag, f);
    }

    @Override
    public void writeFloatArray(String tag, Collection<Float> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeDouble(String tag, double d) throws IOException
    {
        out.put(tag, d);
    }

    @Override
    public void writeDoubleArray(String tag, Collection<Double> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeString(String tag, String s) throws IOException
    {
        out.put(tag, s);
    }

    @Override
    public void writeStringArray(String tag, Collection<String> arr) throws IOException
    {
        out.put(tag, arr);
    }

    @Override
    public void writeBuffer(String tag, byte[] buf) throws IOException
    {
        out.put(tag, buf);
    }

    @Override
    public <T extends ISerializable> void writeObjectArray(String tag, List<T> arr) throws IOException
    {
        List<Map<String, Object>> newArr = new ArrayList<>();
        for (T t : arr)
        {
            JsonOutput subArchive = JsonOutput.createArchive();
            t.write(subArchive);
            newArr.add(subArchive.getOutMap());
        }
        out.put(tag, newArr);
    }

    @Override
    public void writeRawBuffer(String tag, byte[] buf) throws IOException
    {
        out.put(tag, buf);
    }

    @Override
    public void writeByteNullable(String tag, Byte val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeIntegerNullable(String tag, Integer val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeShortNullable(String tag, Short val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeLongNullable(String tag, Long val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeDoubleNullable(String tag, Double val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeFloatNullable(String tag, Float val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeBooleanNullable(String tag, Boolean val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeStringNullable(String tag, String val) throws IOException
    {
        out.put(tag, val);
    }

    @Override
    public void writeShortLE(String tag, short s) throws IOException
    {
        out.put(tag, NetworkUtil.shortBE2LE(s));
    }

    @Override
    public void writeIntegerLE(String tag, int s) throws IOException
    {
        out.put(tag, NetworkUtil.intBE2LE(s));
    }

    @Override
    public void writeLongLE(String tag, long s) throws IOException
    {
        out.put(tag, NetworkUtil.longBE2LE(s));
    }

    @Override
    public void writeStringMap(String tag, Map<String, String> map) throws IOException
    {
        out.put(tag, map);
    }

    @Override
    public <T extends ISerializable> void writeObjectMap(String tag, Map<String, T> map) throws IOException
    {
        if (map != null && map.size() > 0)
        {
            Map<String, Object> jsonMap = new HashMap<>();
            for (Map.Entry<String, T> entry : map.entrySet())
            {
                JsonOutput subArchive = JsonOutput.createArchive();
                entry.getValue().write(subArchive);
                Map<String, Object> subMap = subArchive.getOutMap();
                jsonMap.put(entry.getKey(), subMap);
            }
            out.put(tag, jsonMap);
        }
    }

    /********************************************************************************
     * exception
     ********************************************************************************/

    private void throwNotSupportOperation()
    {
        throw new RuntimeException("JsonOutput not support operation");
    }

    @Override
    public void writeByte(byte b) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeByteArray(Collection<Byte> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeBoolean(boolean b) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeBooleanArray(Collection<Boolean> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeShort(short s) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeShortArray(Collection<Short> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeInteger(int i) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeIntegerArray(Collection<Integer> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeLong(long l) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeLongArray(Collection<Long> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeFloat(float f) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeFloatArray(Collection<Float> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeDouble(double d) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeDoubleArray(Collection<Double> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeString(String s) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeStringArray(Collection<String> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeBuffer(byte[] buf) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeShortLE(short s) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeIntegerLE(int s) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeLongLE(long s) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeStringMap(Map<String, String> map) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public void writeRawBuffer(byte[] buf) throws IOException
    {
        throwNotSupportOperation();
    }

}
