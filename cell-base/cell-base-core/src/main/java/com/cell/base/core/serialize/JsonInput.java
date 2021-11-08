package com.cell.base.core.serialize;

import com.cell.base.common.utils.NetworkUtil;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.json.IJsonArray;
import com.cell.base.core.json.IJsonObject;
import com.cell.base.core.json.PacketJSON;

import java.io.IOException;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:31
 */

public class JsonInput implements IInputArchive
{
    private IJsonObject in;

    public static JsonInput createArchive(byte[] bytes)
    {
        return new JsonInput(bytes);
    }

    public static JsonInput createArchive(String jsonStr)
    {
        return new JsonInput(jsonStr);
    }

    public JsonInput(IJsonObject jsonObject)
    {
        this.in = jsonObject;
    }

    public JsonInput(String jsonStr)
    {
        this(PacketJSON.parseObject(jsonStr));
    }

    public JsonInput(byte[] bytes)
    {
        this(new String(bytes));
    }

    /********************************************************************************
     * json serialize
     ********************************************************************************/

    @Override
    public IInputArchive readSubArchive(String tag) throws IOException
    {
        IJsonObject subJsonObject = in.getJsonObject(tag);
        if (subJsonObject == null)
        {
            return null;
        }
        return new JsonInput(subJsonObject);
    }

    @Override
    public byte readByte(String tag) throws IOException
    {
        return in.getByteValue(tag);
    }

    @Override
    public List<Byte> readByteArray(String tag) throws IOException
    {
        return in.getByteArray(tag);
    }

    @Override
    public byte[] readRawBuffer(String tag, int len) throws IOException
    {
        byte[] source = in.getBuffer(tag);
        byte[] target = new byte[len];
        if (source != null)
        {
            for (int i = 0; i < source.length; i++)
            {
                if (i >= len)
                {
                    break;
                }
                target[i] = source[i];
            }
        }
        return target;
    }

    @Override
    public <T extends ISerializable> T readObjectNullable(String tag, Class<T> clazz) throws IOException
    {
        try
        {
            IInputArchive archive = readSubArchive(tag);
            if (archive != null)
            {
                T t = clazz.newInstance();
                t.read(archive);
                return t;
            } else
            {
                return null;
            }
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new IOException(e);
        }
    }

    @Override
    public <T extends ISerializable> List<T> readObjectArray(String tag, Class<T> clazz) throws IOException
    {
        try
        {
            List<T> arr = new ArrayList<>();
            IJsonArray jsonArray = in.getJsonArray(tag);
            if (jsonArray != null)
            {
                for (int i = 0; i < jsonArray.size(); i++)
                {
                    JsonInput archive = new JsonInput(jsonArray.getObject(i));
                    T t = clazz.newInstance();
                    t.read(archive);
                    arr.add(t);
                }
            }
            return arr;
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new IOException(e);
        }
    }

    @Override
    public Byte readByteNullable(String tag) throws IOException
    {
        return in.getByte(tag);
    }

    @Override
    public Integer readIntegerNullable(String tag) throws IOException
    {
        return in.getInt(tag);
    }

    @Override
    public Short readShortNullable(String tag) throws IOException
    {
        return in.getShort(tag);
    }

    @Override
    public Long readLongNullable(String tag) throws IOException
    {
        return in.getLong(tag);
    }

    @Override
    public Double readDoubleNullable(String tag) throws IOException
    {
        return in.getDouble(tag);
    }

    @Override
    public Float readFloatNullable(String tag) throws IOException
    {
        return in.getFloat(tag);
    }

    @Override
    public Boolean readBooleanNullable(String tag) throws IOException
    {
        return in.getBoolean(tag);
    }

    @Override
    public String readStringNullable(String tag) throws IOException
    {
        return in.getString(tag);
    }

    @Override
    public boolean readBoolean(String tag) throws IOException
    {
        return in.getBooleanValue(tag);
    }

    @Override
    public List<Boolean> readBooleanArray(String tag) throws IOException
    {
        return in.getBooleanArray(tag);
    }

    @Override
    public short readShort(String tag) throws IOException
    {
        return in.getShortValue(tag);
    }

    @Override
    public List<Short> readShortArray(String tag) throws IOException
    {
        return in.getShortArray(tag);
    }

    @Override
    public int readInteger(String tag) throws IOException
    {
        return in.getIntValue(tag);
    }

    @Override
    public List<Integer> readIntegerArray(String tag) throws IOException
    {
        return in.getIntArray(tag);
    }

    @Override
    public TreeSet<Integer> readIntegerTreeSet(String tag) throws IOException
    {
        List<Integer> list = in.getIntArray(tag);
        if (list != null)
        {
            return new TreeSet<>(list);
        }
        return null;
    }

    @Override
    public long readLong(String tag) throws IOException
    {
        return in.getLongValue(tag);
    }

    @Override
    public List<Long> readLongArray(String tag) throws IOException
    {
        return in.getLongArray(tag);
    }

    @Override
    public float readFloat(String tag) throws IOException
    {
        return in.getFloatValue(tag);
    }

    @Override
    public List<Float> readFloatArray(String tag) throws IOException
    {
        return in.getFloatArray(tag);
    }

    @Override
    public double readDouble(String tag) throws IOException
    {
        return in.getDoubleValue(tag);
    }

    @Override
    public List<Double> readDoubleArray(String tag) throws IOException
    {
        return in.getDoubleArray(tag);
    }

    @Override
    public String readString(String tag) throws IOException
    {
        String ret = in.getString(tag);
        if (StringUtils.isEmpty(ret))
        {
            throw new IllegalArgumentException("参数:" + tag + ",不可为空");
        }
        return ret;
    }

    @Override
    public List<String> readStringArray(String tag) throws IOException
    {
        return in.getStringArray(tag);
    }

    @Override
    public void readStringArray(String tag, Collection<String> arr) throws IOException
    {
        List<String> newArr = in.getStringArray(tag);
        if (newArr != null && newArr.size() > 0)
        {
            arr.addAll(newArr);
        }
    }

    @Override
    public long readLongLE(String tag) throws IOException
    {
        Long v = in.getLongValue(tag);
        if (v != null)
        {
            return NetworkUtil.longLE2BE(v);
        } else
        {
            return 0;
        }
    }

    @Override
    public int readIntegerLE(String tag) throws IOException
    {
        final Integer v = in.getIntValue(tag);
        if (v != null)
        {
            return NetworkUtil.intLE2BE(v);
        } else
        {
            return 0;
        }
    }

    @Override
    public short readShortLE(String tag) throws IOException
    {
        Short v = in.getShortValue(tag);
        if (v != null)
        {
            return NetworkUtil.shortLE2BE(v);
        } else
        {
            return 0;
        }
    }

    @Override
    public byte[] readBuffer(String tag) throws IOException
    {
        return in.getBuffer(tag);
    }

    @Override
    public Map<String, String> readStringMap(String tag) throws IOException
    {
        Map<String, String> res = new HashMap<>();
        IJsonObject jsonObj = in.getJsonObject(tag);
        if (jsonObj != null)
        {
            for (String key : jsonObj.keys())
            {
                String value = jsonObj.getString(key);
                res.put(key, value);
            }
        }
        return res;
    }

    @Override
    public <T extends ISerializable> Map<String, T> readObjectMap(String tag, Class<T> clazz) throws IOException
    {
        Map<String, T> res = new HashMap<>();
        try
        {
            IJsonObject jsonObj = in.getJsonObject(tag);
            if (jsonObj != null)
            {
                for (String key : jsonObj.keys())
                {
                    IJsonObject jsonObject = jsonObj.getJsonObject(key);
                    if (jsonObject != null)
                    {
                        JsonInput archive = new JsonInput(jsonObject);
                        T t = clazz.newInstance();
                        t.read(archive);
                        res.put(key, t);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new IOException(e);
        }
        return res;
    }

    /********************************************************************************
     * exception
     ********************************************************************************/

    private void throwNotSupportOperation()
    {
        throw new RuntimeException("JsonInput not support operation");
    }

    @Override
    public byte readByte() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public List<Byte> readByteArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public boolean readBoolean() throws IOException
    {
        throwNotSupportOperation();
        return false;
    }

    @Override
    public List<Boolean> readBooleanArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public short readShort() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public List<Short> readShortArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public int readInteger() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public List<Integer> readIntegerArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public long readLong() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public List<Long> readLongArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public float readFloat() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public List<Float> readFloatArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public double readDouble() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public List<Double> readDoubleArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public String readString() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public List<String> readStringArray() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public void readStringArray(Collection<String> arr) throws IOException
    {
        throwNotSupportOperation();
    }

    @Override
    public byte[] readBuffer() throws IOException
    {
        throwNotSupportOperation();
        return new byte[0];
    }

    @Override
    public int skipBytes(int n) throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public long readLongLE() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public int readIntegerLE() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public short readShortLE() throws IOException
    {
        throwNotSupportOperation();
        return 0;
    }

    @Override
    public Map<String, String> readStringMap() throws IOException
    {
        throwNotSupportOperation();
        return null;
    }

    @Override
    public byte[] readRawBuffer(int len) throws IOException
    {
        throwNotSupportOperation();
        return new byte[0];
    }

    /********************************************************************************
     * test
     ********************************************************************************/

    public static void main(String[] args) throws IOException
    {
        JsonOutput output = JsonOutput.createArchive();
        output.writeString("str1", "a1");
        output.writeString("str2", null);
        output.writeInteger("int1", 1);
        output.writeIntegerLE("int2", 1);
        output.writeBuffer("buffer1", "buffer".getBytes());
        Map outMap1 = new HashMap<String, String>();
        outMap1.put("a", "a");
        outMap1.put("b", "b");
        output.writeStringMap("map1", outMap1);
        List<String> outArr1 = new ArrayList<>();
        outArr1.add("a");
        outArr1.add("b");
        output.writeStringArray("arr1", outArr1);
        output.subArchive("header").writeInteger("int", 100);

        JsonInput input = JsonInput.createArchive(output.toBytes());
        System.out.println(input.readString("str1"));
        System.out.println(input.readString("str2"));
        System.out.println(input.readInteger("int1"));
        System.out.println(input.readIntegerLE("int2"));
        byte[] buffer1s = input.readBuffer("buffer1");
        System.out.println(new String(buffer1s));
        System.out.println(input.readStringMap("map1"));
        List<String> arr1 = input.readStringArray("arr1");
        System.out.println(arr1);
        IInputArchive header = input.readSubArchive("header");
        if (header != null)
        {
            int headerInt = header.readInteger("int");
            System.out.println(headerInt);
        }
    }

}