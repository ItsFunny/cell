package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-23 16:39
 */
public enum TimeWheelDeleyLevel
{
    HOUR((byte) 0),
    MIN((byte) 1),
    SECONDS((byte) 2),
    ;

    byte index;
    TimeWheelDeleyLevel(byte index)
    {
        this.index = index;
    }

    public byte getIndex()
    {
        return index;
    }

    public void setIndex(byte index)
    {
        this.index = index;
    }}
