package com.cell.base.core.utils;

import org.junit.Test;

public class ClassUtilTest
{

    @Test
    public void getMainApplicationClass()
    {
    }

    public static void main(String[] args)
    {
        Class<?> clz = ClassUtil.getMainApplicationClass();
        System.out.println(clz.getName());
        Package aPackage = clz.getPackage();
        String name = aPackage.getName();
        String[] split = name.split("\\.");
        for (String s : split)
        {
            System.out.println(s);
        }
        System.out.println(clz.getPackage());
    }
}