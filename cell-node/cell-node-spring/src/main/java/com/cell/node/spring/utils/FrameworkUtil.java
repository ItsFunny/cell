package com.cell.node.spring.utils;

import com.cell.node.spring.bridge.ISpringNodeExtension;
import com.cell.base.core.utils.ClassUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-26 05:09
 */
public class FrameworkUtil
{
    public static boolean checkIsExtension(Class c)
    {
        return ISpringNodeExtension.class.isAssignableFrom(c) && !ClassUtil.checkIsAbstract(c);
    }


    public static <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args)
    {
        ClassLoader classLoader = new DefaultResourceLoader().getClassLoader();
        // Use names and ensure unique to protect against duplicates
        Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
        List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
        AnnotationAwareOrderComparator.sort(instances);
        return instances;
    }

    public static <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,
                                                             ClassLoader classLoader, Object[] args, Set<String> names)
    {
        List<T> instances = new ArrayList<>(names.size());
        for (String name : names)
        {
            try
            {
                Class<?> instanceClass = ClassUtils.forName(name, classLoader);
                Assert.isAssignable(type, instanceClass);
                Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
                T instance = (T) BeanUtils.instantiateClass(constructor, args);
                instances.add(instance);
            } catch (Throwable ex)
            {
                throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);
            }
        }
        return instances;
    }
}
