package com.cell.utils;

import com.cell.annotations.Optional;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.ISerializable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static void getObjectFileAnnotation(final Object obj, Annotation... annotation)
    {
        Field[] fields = obj.getClass().getFields();

    }

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
    public static Object newInstance(Class<?> clazz, Object... args)
    {
        try
        {
            checkPackageAccess(clazz);
            return clazz.newInstance();
        } catch (Exception e)
        {
            try
            {
                Constructor<?> declaredConstructor;
                if (args != null && args.length > 0)
                {
                    Class[] classes = Arrays.asList(args).stream().map(p -> p.getClass()).collect(Collectors.toList()).toArray(new Class[args.length]);
                    declaredConstructor = clazz.getDeclaredConstructor(classes);
                } else
                {
                    declaredConstructor = clazz.getDeclaredConstructor();
                }
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance();
            } catch (Exception e1)
            {
                throw new ProgramaException(e);
            }
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

    public static void modify(AnnotatedElement element,
                              Class<? extends Annotation> annotationClass,
                              String key, Object value)
    {
        try
        {
            Annotation annotationToBeModified = element.getAnnotation(annotationClass);
            if (annotationToBeModified == null) return;
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotationToBeModified);
            Field memberValuesField;
            memberValuesField = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValuesField.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) memberValuesField.get(invocationHandler);
            memberValues.put(key, value);
        } catch (Exception e)
        {
            throw new ProgramaException(e);
        }
    }

    public static void checkPackageAccess(Class<?> var0)
    {
        checkPackageAccess(var0.getName());
        if (isNonPublicProxyClass(var0))
        {
            checkProxyPackageAccess(var0);
        }
    }

    public static void checkPackageAccess(String var0)
    {
        SecurityManager var1 = System.getSecurityManager();
        if (var1 != null)
        {
            String var2 = var0.replace('/', '.');
            int var3;
            if (var2.startsWith("["))
            {
                var3 = var2.lastIndexOf(91) + 2;
                if (var3 > 1 && var3 < var2.length())
                {
                    var2 = var2.substring(var3);
                }
            }

            var3 = var2.lastIndexOf(46);
            if (var3 != -1)
            {
                var1.checkPackageAccess(var2.substring(0, var3));
            }
        }

    }

    public static boolean isNonPublicProxyClass(Class<?> var0)
    {
        String var1 = var0.getName();
        int var2 = var1.lastIndexOf(46);
        String var3 = var2 != -1 ? var1.substring(0, var2) : "";
        return Proxy.isProxyClass(var0) && !var3.equals("com.sun.proxy");
    }

    public static void checkProxyPackageAccess(Class<?> var0)
    {
        SecurityManager var1 = System.getSecurityManager();
        if (var1 != null && Proxy.isProxyClass(var0))
        {
            Class[] var2 = var0.getInterfaces();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                Class var5 = var2[var4];
                checkPackageAccess(var5);
            }
        }

    }

    public static void checkProxyPackageAccess(ClassLoader var0, Class... var1)
    {
        SecurityManager var2 = System.getSecurityManager();
        if (var2 != null)
        {
            Class[] var3 = var1;
            int var4 = var1.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                Class var6 = var3[var5];
                ClassLoader var7 = var6.getClassLoader();
                if (needsPackageAccessCheck(var0, var7))
                {
                    checkPackageAccess(var6);
                }
            }
        }
    }

    public static boolean needsPackageAccessCheck(ClassLoader var0, ClassLoader var1)
    {
        if (var0 != null && var0 != var1)
        {
            if (var1 == null)
            {
                return true;
            } else
            {
                return !isAncestor(var0, var1);
            }
        } else
        {
            return false;
        }
    }

    private static boolean isAncestor(ClassLoader var0, ClassLoader var1)
    {
        ClassLoader var2 = var1;

        do
        {
            var2 = var2.getParent();
            if (var0 == var2)
            {
                return true;
            }
        }
        while (var2 != null);

        return false;
    }

    public static Object instanceFill(Class<?> bzClz, IInputArchive inputArchive) throws Exception
    {
        Object instance = null;
        if (ISerializable.class.isAssignableFrom(bzClz))
        {
            instance = bzClz.newInstance();
            ((ISerializable) instance).read(inputArchive);
        } else
        {
            instance = reflectFill(bzClz, inputArchive);
        }
        return instance;
    }

    private static Object reflectFill(Class<?> clz, IInputArchive inputArchive) throws IOException
    {
        Field[] fields = clz.getDeclaredFields();
        BeanWrapper beanWrapper = new BeanWrapperImpl(clz);
        for (Field field : fields)
        {
            String name = field.getName();
            if (name.startsWith("abs")) continue;
            Optional annotation = field.getAnnotation(Optional.class);
            if (annotation == null)
            {
                beanWrapper.setPropertyValue(name, inputArchive.readString(name));
            } else
            {
                beanWrapper.setPropertyValue(name, inputArchive.readStringNullable(name));
            }
        }
        return beanWrapper.getWrappedInstance();

    }
}

