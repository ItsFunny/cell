package com.cell.base.core.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cell.base.core.json.IJsonArray;
import com.cell.base.core.json.IJsonObject;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:39
 */
class FastJsonArray implements IJsonArray
{
    private JSONArray in;

    public static IJsonArray parseArray(String jsonStr)
    {
        JSONArray jsonArray = JSON.parseArray(jsonStr);
        return new FastJsonArray(jsonArray);
    }

    public FastJsonArray(String jsonStr)
    {
        this(JSON.parseArray(jsonStr));
    }

    FastJsonArray(JSONArray jsonArray)
    {
        this.in = jsonArray;
    }

    @Override
    public int size()
    {
        return in != null ? in.size() : 0;
    }

    @Override
    public byte getByteValue(int index)
    {
        return in.getByteValue(index);
    }

    @Override
    public boolean getBooleanValue(int index)
    {
        return in.getBooleanValue(index);
    }

    @Override
    public short getShortValue(int index)
    {
        return in.getShortValue(index);
    }

    @Override
    public int getIntValue(int index)
    {
        return in.getIntValue(index);
    }

    @Override
    public long getLongValue(int index)
    {
        return in.getLongValue(index);
    }

    @Override
    public float getFloatValue(int index)
    {
        return in.getFloatValue(index);
    }

    @Override
    public double getDoubleValue(int index)
    {
        return in.getDoubleValue(index);
    }

    @Override
    public String getString(int index)
    {
        return in.getString(index);
    }

    public IJsonObject getObject(int index)
    {
        JSONObject jsonObject = in.getJSONObject(index);
        if (jsonObject != null)
        {
            return new FastJsonObject(jsonObject);
        } else
        {
            return null;
        }
    }
}

