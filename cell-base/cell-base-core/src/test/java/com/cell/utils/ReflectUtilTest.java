package com.cell.utils;

import com.cell.annotations.ForceOverride;
import com.cell.annotations.ReactorAnno;
import com.cell.protocol.ICommand;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;

public class ReflectUtilTest
{
    @ReactorAnno(group = "asd")
    public static class AAAA
    {

    }

    @Test
    public void modify() throws Exception
    {
        AAAA aa = new AAAA();
        ReactorAnno newAnno = new ReactorAnno()
        {
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return null;
            }

            @Override
            public ForceOverride withForce()
            {
                return new ForceOverride()
                {
                    @Override
                    public Class<? extends Annotation> annotationType()
                    {
                        return ForceOverride.class;
                    }

                    @Override
                    public boolean forceOverride()
                    {
                        return false;
                    }
                };
            }

            @Override
            public Class<? extends ICommand>[] cmds()
            {
                return new Class[0];
            }

            @Override
            public String group()
            {
                return "zzzzzzzz";
            }
        };
        Class<? extends AAAA> aClass = aa.getClass();
        ReflectUtil.modify(aClass, ReactorAnno.class, "group", "zzzzzzz");
        Object o = ReflectUtil.newInstance(aClass);
        ReactorAnno annotation = o.getClass().getAnnotation(ReactorAnno.class);
        System.out.println(annotation.group());
    }

    public static class AAA
    {

    }

    @Test
    public void testModifyAttrituteRuntime() throws Exception
    {
        AAA a = new AAA();

        ByteBuddyAgent.install();
        new ByteBuddy()
                .redefine(a.getClass())
                .annotateType(AnnotationDescription.Builder.ofType(ReactorAnno.class).define("group", "asd").build())
                .make().load(ClassLoader.getSystemClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        ReactorAnno annotation = a.getClass().getAnnotation(ReactorAnno.class);
        Assert.assertNotNull(annotation);
        System.out.println(annotation.group());

        AAA ba = new AAA();
        System.out.println(ba.getClass().getAnnotation(ReactorAnno.class).group());
    }

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