package com.cell.http.gate.config;

import io.netty.util.internal.ConcurrentSet;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 22:23
 */
// FIXME 通过componentScan解决
public class Config
{
    private static final Set<Class<?>> interestClasses = new ConcurrentSet<>();
    private static final Set<Class<? extends Annotation>> interestAnnotations = new ConcurrentSet<>();

    public static void addInterestClasses(Class<?> c)
    {
        interestClasses.add(c);
    }

    public static void addInterestAnnotations(Class<? extends Annotation> c)
    {
        interestAnnotations.add(c);
    }


    public static Set<Class<?>> getInterestClasses()
    {
        return interestClasses;
    }

    public static Set<Class<? extends  Annotation>> getInterestAnnotations()
    {
        return interestAnnotations;
    }
}
