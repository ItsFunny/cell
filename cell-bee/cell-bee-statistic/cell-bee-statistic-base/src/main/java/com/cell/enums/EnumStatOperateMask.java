package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 20:26
 */
public enum EnumStatOperateMask
{

    ORIGIN(1, "对象原始的统计功能"),
    MIN_VALUE(1 << 1, "最小值"),
    MAX_VALUE(1 << 2, "最大值"),
    AVERAGE(1 << 3, "平均值"),
    STANDARD_DEVIATION(1 << 4, "标准差"),
    ;

    private int value;
    private String desc;

    private EnumStatOperateMask(int value, String desc)
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

}
