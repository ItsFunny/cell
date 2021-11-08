package com.cell.base.core.json;

import com.cell.base.core.json.fastjson.FastJsonParser;

/**
 * @author Charlie
 * @When
 * @Description fastJson的封装类
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:34
 */
public class PacketJSON
{

    private static final IJsonParser jsonParser = new FastJsonParser();

    public static IJsonObject parseObject(String jsonStr)
    {
        return jsonParser.parseObject(jsonStr);
    }

    public static IJsonArray parseArray(String jsonStr)
    {
        return jsonParser.parseArray(jsonStr);
    }

    public static String toJsonString(Object object)
    {
        return jsonParser.toJsonString(object);
    }
}
