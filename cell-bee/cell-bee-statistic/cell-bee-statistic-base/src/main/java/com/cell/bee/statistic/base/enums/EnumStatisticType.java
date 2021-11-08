package com.cell.bee.statistic.base.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 20:24
 */
public enum EnumStatisticType
{
    SECOND(1, "秒"), MINUTE(2, "分钟"),

    HOUR(3, "小时"), DAY(4, "天");

    private int value;
    private String desc;

    private EnumStatisticType(int value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public int getValue()
    {
        return value;
    }

    public String getDesc()
    {
        return desc;
    }

    public static final Map<Integer, EnumStatisticType> CACHE = new HashMap<>(EnumStatisticType.values().length);

    static
    {
        for (EnumStatisticType type : EnumStatisticType.values())
        {
            CACHE.put(type.getValue(), type);
        }
    }

    public static EnumStatisticType fromValue(Integer value)
    {
        if (value == null)
        {
            return SECOND;
        }
        return CACHE.getOrDefault(value, SECOND);
    }
}
