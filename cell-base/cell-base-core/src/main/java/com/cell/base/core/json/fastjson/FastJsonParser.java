package com.cell.base.core.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.cell.base.core.json.IJsonArray;
import com.cell.base.core.json.IJsonObject;
import com.cell.base.core.json.IJsonParser;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:38
 */
public class FastJsonParser implements IJsonParser
{

    public IJsonObject parseObject(String jsonStr)
    {
        return FastJsonObject.parseObject(jsonStr);
    }

    public IJsonArray parseArray(String jsonStr)
    {
        return FastJsonArray.parseArray(jsonStr);
    }

    public String toJsonString(Object object)
    {
        String json = JSON.toJSONString(object);
//        json = json.replace("\\", "");
//        json = json.replace("\"{", "{");
//        json = json.replace("}\"", "}");
//        json = json.replace("\"[", "]");
//        json = json.replace("]\"", "]");
        return json;
    }

}
