package com.cell.comparators;

import com.cell.annotations.CellOrder;
import com.cell.constants.Constants;
import com.cell.utils.ClassUtil;

import java.util.Comparator;

public class OrderComparator implements Comparator<Class<?>>
{

    @Override
    public int compare(Class<?> o1, Class<?> o2)
    {
        return ClassUtil.ordererCompare(o1, o2);
    }

}
