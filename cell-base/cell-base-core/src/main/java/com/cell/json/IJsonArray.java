package com.cell.json;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:32
 */

public interface IJsonArray {
    int size();

    byte getByteValue(int index);
    boolean getBooleanValue(int index);
    short getShortValue(int index);
    int getIntValue(int index);
    long getLongValue(int index);
    float getFloatValue(int index);
    double getDoubleValue(int index);
    String getString(int index);
    IJsonObject getObject(int index);
}
