package com.cell.initializer;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.common.utils.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-16 14:46
 */
public class AnnotationTest
{

    public static class ABS
    {
        @AutoPlugin
        private String st;
    }

    @Test
    public void testAnnotation()
    {
        ABS a = new ABS();
        Optional<AutoPlugin> annotation = AnnotationUtils.findAnnotation(a.getClass(), AutoPlugin.class);
        System.out.println(annotation.isPresent());
        List<Field> annotatedFields = AnnotationUtils.findAnnotatedFields(a.getClass(), AutoPlugin.class, (c) -> true);
        for (Field annotatedField : annotatedFields)
        {
        }
        System.out.println(annotatedFields);
    }

    public static class A
    {
        @AutoPlugin
        private void method1()
        {

        }
    }

    @Test
    public void test2()
    {
        A a2 = new A();
        boolean b = ReflectionUtils.containAnnotaitonsInFieldOrMethod(a2.getClass(), AutoPlugin.class);
        System.out.println(b);
        Assert.assertTrue(b);
    }

    @Test
    public void test3() throws Exception
    {
        A a2 = new A();
//        Mono<Boolean> booleanMono = ReflectionUtils.containAnnotaitonsInFieldOrMethod(a2.getClass(), BitConstants.or, AutoPlugin.class, Autowired.class);
//        booleanMono.subscribe((v) ->
//                System.out.println("exists"));
//        boolean b = ReflectionUtils.containAnnotaitonsInFieldOrMethod(a2.getClass(), BitConstants.or, AutoPlugin.class, Autowired.class);
//        System.out.println(b);
//        Assert.assertTrue(b);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void testMono()
    {
    }

    @Test
    public void testCount()
    {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try
        {
            countDownLatch.await(1, TimeUnit.SECONDS);
            long count = countDownLatch.getCount();
            System.out.println(count);
        } catch (InterruptedException e)
        {
            System.out.println(e);
        }
    }

    public static void main(String[] args)
    {
        Integer a = 1;
    }
}
