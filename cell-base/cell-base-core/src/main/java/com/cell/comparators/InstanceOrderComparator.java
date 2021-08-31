package com.cell.comparators;

import com.cell.utils.ClassUtil;

import java.util.Comparator;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 20:13
 */
public class InstanceOrderComparator implements Comparator<Object>
{
    private boolean desc;

    public InstanceOrderComparator(boolean desc)
    {
        this.desc = desc;
    }

    @Override
    public int compare(Object o1, Object o2)
    {
        Class<?> a1 = o1.getClass();
        Class<?> a2 = o2.getClass();
        if (desc)
        {
            return -ClassUtil.ordererCompare(a1, a2);
        }
        return ClassUtil.ordererCompare(a1, a2);
    }
}
