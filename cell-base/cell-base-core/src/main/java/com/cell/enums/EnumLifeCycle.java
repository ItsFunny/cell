package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 08:29
 */
public enum EnumLifeCycle
{
    ONCE((byte) 0),
    LONG_LIFE((byte) 1),
    ;
    byte flag;

    EnumLifeCycle(byte i)
    {
        flag = i;
    }}
