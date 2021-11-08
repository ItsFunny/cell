package com.cell.base.core.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cell.base.core.json.IJsonArray;
import com.cell.base.core.json.IJsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:39
 */
class FastJsonObject implements IJsonObject
{

    private JSONObject in;

    public static IJsonObject parseObject(String jsonStr)
    {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return new FastJsonObject(jsonObject);
    }

    public FastJsonObject(String jsonStr)
    {
        this(JSON.parseObject(jsonStr));
    }

    FastJsonObject(JSONObject jsonObject)
    {
        this.in = jsonObject;
        if (this.in == null)
        {
            this.in = new JSONObject();
        }
    }

    @Override
    public IJsonArray getJsonArray(String tag)
    {
        JSONArray jsonArray = in.getJSONArray(tag);
        return new FastJsonArray(jsonArray);
    }

    @Override
    public IJsonObject getJsonObject(String tag)
    {
        JSONObject jsonObject = in.getJSONObject(tag);
        if (jsonObject == null)
        {
            return null;
        }
        return new FastJsonObject(jsonObject);
    }

    @Override
    public Set<String> keys()
    {
        return in.getInnerMap().keySet();
    }

    @Override
    public Byte getByte(String tag)
    {
        return in.getByte(tag);
    }

    @Override
    public byte getByteValue(String tag)
    {
        return in.getByteValue(tag);
    }

    @Override
    public List<Byte> getByteArray(String tag)
    {
        Byte[] arr = in.getObject(tag, Byte[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public Boolean getBoolean(String tag)
    {
        return in.getBoolean(tag);
    }

    @Override
    public boolean getBooleanValue(String tag)
    {
        return in.getBooleanValue(tag);
    }

    @Override
    public List<Boolean> getBooleanArray(String tag)
    {
        Boolean[] arr = in.getObject(tag, Boolean[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public Short getShort(String tag)
    {
        return in.getShort(tag);
    }

    @Override
    public short getShortValue(String tag)
    {
        return in.getShortValue(tag);
    }

    @Override
    public List<Short> getShortArray(String tag)
    {
        Short[] arr = in.getObject(tag, Short[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public Integer getInt(String tag)
    {
        return in.getInteger(tag);
    }

    @Override
    public int getIntValue(String tag)
    {
        return in.getIntValue(tag);
    }

    @Override
    public List<Integer> getIntArray(String tag)
    {
        Integer[] arr = in.getObject(tag, Integer[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public Long getLong(String tag)
    {
        return in.getLong(tag);
    }

    @Override
    public long getLongValue(String tag)
    {
        return in.getLongValue(tag);
    }

    @Override
    public List<Long> getLongArray(String tag)
    {
        Long[] arr = in.getObject(tag, Long[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public Float getFloat(String tag)
    {
        return in.getFloat(tag);
    }

    @Override
    public float getFloatValue(String tag)
    {
        return in.getFloatValue(tag);
    }

    @Override
    public List<Float> getFloatArray(String tag)
    {
        Float[] arr = in.getObject(tag, Float[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public Double getDouble(String tag)
    {
        return in.getDouble(tag);
    }

    @Override
    public double getDoubleValue(String tag)
    {
        return in.getDoubleValue(tag);
    }

    @Override
    public List<Double> getDoubleArray(String tag)
    {
        Double[] arr = in.getObject(tag, Double[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public String getString(String tag)
    {
        return in.getString(tag);
    }

    @Override
    public List<String> getStringArray(String tag)
    {
        String[] arr = in.getObject(tag, String[].class);
        if (arr != null)
        {
            return Arrays.asList(arr);
        } else
        {
            return null;
        }
    }

    @Override
    public byte[] getBuffer(String tag)
    {
        return in.getObject(tag, byte[].class);
    }

//    @Override
//    public <T> List<T> getObjectArray(String tag, Class<T> clazz) {
//        String str = in.getString(tag);
//        if (!StringUtil.isNullEmpty(str)){
//            return JSON.parseArray(str, clazz);
//        }
//        return null;
//    }

}
