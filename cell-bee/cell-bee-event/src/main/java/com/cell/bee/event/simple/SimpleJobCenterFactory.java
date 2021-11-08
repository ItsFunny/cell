package com.cell.bee.event.simple;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 09:58
 */
public class SimpleJobCenterFactory
{
    public static SimpleJobCenter NewSimpleJobCenter()
    {
        SimpleJobCenter ret = new SimpleJobCenter();
        return ret;
    }
}
