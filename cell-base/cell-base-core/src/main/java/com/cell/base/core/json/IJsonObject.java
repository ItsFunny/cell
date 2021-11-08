package com.cell.base.core.json;

import java.util.List;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:31
 */
public interface IJsonObject
{

    IJsonArray getJsonArray(String tag);

    IJsonObject getJsonObject(String tag);

    byte[] getBuffer(String tag);

    Set<String> keys();

    Byte getByte(String tag);

    byte getByteValue(String tag);

    List<Byte> getByteArray(String tag);

    Boolean getBoolean(String tag);

    boolean getBooleanValue(String tag);

    List<Boolean> getBooleanArray(String tag);

    Short getShort(String tag);

    short getShortValue(String tag);

    List<Short> getShortArray(String tag);

    Integer getInt(String tag);

    int getIntValue(String tag);

    List<Integer> getIntArray(String tag);

    Long getLong(String tag);

    long getLongValue(String tag);

    List<Long> getLongArray(String tag);

    Float getFloat(String tag);

    float getFloatValue(String tag);

    List<Float> getFloatArray(String tag);

    Double getDouble(String tag);

    double getDoubleValue(String tag);

    List<Double> getDoubleArray(String tag);

    String getString(String tag);

    List<String> getStringArray(String tag);

}
