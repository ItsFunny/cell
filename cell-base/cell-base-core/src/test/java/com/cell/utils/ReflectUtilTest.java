package com.cell.utils;

import com.cell.annotations.ForceOverride;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;

import static org.junit.Assert.*;

public class ReflectUtilTest
{

    @Aa(name = "asd")
    public static class A
    {

    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Aa
    {
        String name();
    }

    @Test
    public void overRideAnnotationAttribute()
    {
        final String name = "charlie";
        A a = new A();
        Aa annotation = a.getClass().getAnnotation(Aa.class);
        System.out.println(annotation.name());
        Assert.assertEquals(annotation.name(), "asd");

        Annotation newA = new Aa()
        {
            @Override
            public String name()
            {
                return name;
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return Aa.class;
            }
        };
        ReflectUtil.overRideAnnotationOn(a.getClass(), Aa.class, newA);
        annotation = a.getClass().getAnnotation(Aa.class);
        A aaa = new A();
        Aa aaaaAnno = aaa.getClass().getAnnotation(Aa.class);
        System.out.println(aaaaAnno.name());

        System.out.println(annotation.name());
        Assert.assertEquals(annotation.name(), name);
    }
}