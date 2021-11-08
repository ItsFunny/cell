package com.cell.base.core.json;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:34
 */
public interface IJsonParser
{
    IJsonObject parseObject(String jsonStr);

    IJsonArray parseArray(String jsonStr);

    String toJsonString(Object object);
}
