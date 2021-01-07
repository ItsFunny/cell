package com.cell.utils;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-11-13 22:46
 */
public class StringUtils
{
    public static boolean isNullOrEmpty(String str)
    {
        return org.apache.commons.lang3.StringUtils.isEmpty(str);
    }
}
