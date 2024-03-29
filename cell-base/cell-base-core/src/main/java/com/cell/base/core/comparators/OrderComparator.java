package com.cell.base.core.comparators;


import com.cell.base.core.utils.ClassUtil;

import java.util.Comparator;

public class OrderComparator implements Comparator<Class<?>>
{

    @Override
    public int compare(Class<?> o1, Class<?> o2)
    {
        return ClassUtil.ordererCompare(o1, o2);
    }

}
