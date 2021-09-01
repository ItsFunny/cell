package com.cell.utils;

import com.cell.exceptions.ProgramaException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 18:19
 */

public class ReflectUtil
{
    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null)
        {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try
        {
            result = field.get(obj);
        } catch (IllegalAccessException e)
        {
            throw new IllegalArgumentException("get field value error");
        }
        return result;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName)
    {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e)
            {//NOSONAR
                // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
                .isFinal(field.getModifiers())) && !field.isAccessible())
        {
            field.setAccessible(true);
        }
    }

    /**
     * 判断属性是否存在
     *
     * @param fieldName
     * @param clazz
     * @return
     */
    public static Boolean isFieldExist(String fieldName, Class clazz)
    {
        Field[] fields = clazz.getDeclaredFields();//获取所有属性
        for (Field field : fields)
        {
            if (field.getName().equals(fieldName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断属性是否存在
     *
     * @param methodName
     * @param clazz
     * @return
     */
    public static Boolean isMethodExist(String methodName, Class clazz)
    {
        Method[] methods = clazz.getDeclaredMethods();//获取所有方法
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
            {
                return true;
            }
        }
        return false;
    }

    // FIXME
    public static Object newInstance(Class<?> clazz)
    {
        try
        {
            return sun.reflect.misc.ReflectUtil.newInstance(clazz);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static final String ANNOTATIONS = "annotations";
    public static final String ANNOTATION_DATA = "annotationData";

    public static boolean isJDK7OrLower()
    {
        boolean jdk7OrLower = true;
        try
        {
            Class.class.getDeclaredField(ANNOTATIONS);
        } catch (NoSuchFieldException e)
        {
            //Willfully ignore all exceptions
            jdk7OrLower = false;
        }
        return jdk7OrLower;
    }

    public static void overRideAnnotationOn(Class clazzToLookFor, Class<? extends Annotation> annotationToAlter, Annotation annotationValue)
    {
        try
        {
            Method method = Class.class.getDeclaredMethod(ANNOTATION_DATA, null);
            method.setAccessible(true);
            Object annotationData = method.invoke(clazzToLookFor);
            Field annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS);
            annotations.setAccessible(true);
            Map<Class<? extends Annotation>, Annotation> map =
                    (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
            map.put(annotationToAlter, annotationValue);
        } catch (Exception e)
        {
            throw new ProgramaException(e);
        }
//        if (isJDK7OrLower())
//        {
//            try
//            {
//                Field annotations = Class.class.getDeclaredField(ANNOTATIONS);
//                annotations.setAccessible(true);
//                Map<Class<? extends Annotation>, Annotation> map =
//                        (Map<Class<? extends Annotation>, Annotation>) annotations.get(clazzToLookFor);
//                map.put(annotationToAlter, annotationValue);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        } else
//        {
//            try
//            {
//                Method method = Class.class.getDeclaredMethod(ANNOTATION_DATA, null);
//                method.setAccessible(true);
//                Object annotationData = method.invoke(clazzToLookFor);
//                Field annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS);
//                annotations.setAccessible(true);
//                Map<Class<? extends Annotation>, Annotation> map =
//                        (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
//                map.put(annotationToAlter, annotationValue);
//            } catch (Exception e)
//            {
//                throw new ProgramaException(e);
//            }
//        }
    }
}

