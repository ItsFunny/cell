package com.cell.utils;


import com.cell.concurrent.base.DefaultThreadFactory;
import com.cell.log.LOG;
import com.cell.models.Module;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.internal.StringUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类工具类
 * 1、扫描指定包下的所有类
 */
public class ClassUtil
{

    /**
     * Class文件扩展名
     */
    private static final String CLASS_EXT = ".class";
    /**
     * Jar文件扩展名
     */
    private static final String JAR_FILE_EXT = ".jar";
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    private static final String JAR_PATH_EXT = ".jar!";
    /**
     * 当Path为文件形式时, path会加入一个表示文件的前缀
     */
    private static final String PATH_FILE_PRE = "file:";
    /**
     * 扫描Class文件时空的过滤器，表示不过滤
     */
    private static final ClassFilter NULL_CLASS_FILTER = null;


    /**
     * 基本变量类型的枚举
     *
     * @author xiaoleilu
     */
    private static enum BASIC_TYPE
    {
        BYTE, SHORT, INT, INTEGER, LONG, DOUBLE, FLOAT, BOOLEAN, CHAR, CHARACTER, STRING;
    }

    private ClassUtil()
    {
    }

    public static boolean existField(Object obj, String fieldName)
    {
        if (obj == null || StringUtils.isEmpty(fieldName))
        {
            return false;
        }

        Class<?> clazz = obj.getClass();
        try
        {
            clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e)
        {
            LOG.warn(Module.COMMON, "未在对象{}中找到属性{} ", clazz.getSimpleName(), fieldName);
            return false;
        }
        return true;
    }

    /**
     * 扫面改包路径下所有class文件
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return
     */
    public static Set<Class<?>> scanPackage(String packageName)
    {
        LOG.debug(Module.COMMON, "Scan classes from package: {}", packageName);
        return scanPackage(packageName, NULL_CLASS_FILTER);
    }

    public static Object invokeFieldThrowException(Object obj, String fieldName) throws Exception
    {
        Class<?> clazz = obj.getClass();
        try
        {
            String methodName = "get" + StringUtils.firstToUpper(fieldName);
            return invokeMethod(obj, methodName);
        } catch (Exception e)
        {
            LOG.warn(Module.COMMON, "反射Field {}失败， obj = {}", clazz, fieldName, obj);
            String methodName = "is" + StringUtils.firstToUpper(fieldName);
            try
            {
                return invokeMethod(obj, methodName);
            } catch (Exception e2)
            {
                LOG.warning(Module.COMMON, e, "%s反射Field %s失败， obj = %s", clazz, fieldName, obj);
                throw e2;
            }
        }
    }

    public static Object invokeField(Object obj, String fieldName)
    {
        if (obj == null)
        {
            return null;
        }

        Class<?> clazz = obj.getClass();
        try
        {
            Field[] fields = getAllFields(clazz);
            Field field = getField(fields, fieldName);
            if (field == null)
            {
//    			LOG.warning(Module.COMMON, "%s反射Field %s失败， obj = %s", clazz, fieldName, obj);
                return null;
            }
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "%s反射Field %s失败， obj = %s", clazz, fieldName, obj);
        }
        return null;
    }

    public static Field getField(Object obj, String fieldName)
    {
        if (obj == null)
        {
            return null;
        }

        Class<?> clazz = obj.getClass();
        try
        {
            Field[] fields = getAllFields(clazz);
            Field field = getField(fields, fieldName);
            if (field == null)
            {
//    			LOG.warning(Module.COMMON, "%s反射Field %s失败， obj = %s", clazz, fieldName, obj);
                return null;
            }
            field.setAccessible(true);
            return field;
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "%s反射Field %s失败， obj = %s", clazz, fieldName, obj);
        }
        return null;
    }

    private static Field getField(Field[] fields, String fieldName)
    {
        if (StringUtils.isEmpty(fields))
        {
            return null;
        }

        for (Field field : fields)
        {
            if (field.getName().equals(fieldName))
            {
                return field;
            }
        }
        return null;
    }

    public static Field[] getAllFields(Object obj)
    {
        if (obj == null)
        {
            return null;
        }

        return getAllFields(obj.getClass());
    }

    public static Field[] getAllFields(Class<?> clazz)
    {
        Set<Field> set = new HashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        set.addAll(Arrays.asList(fields));
        fields = clazz.getFields();
        set.addAll(Arrays.asList(fields));
        return collectionToArray(set);
    }

    public static Field[] collectionToArray(Collection<Field> collection)
    {
        if (StringUtils.isEmpty(collection))
        {
            return null;
        }

        int size = collection.size();
        Field[] result = new Field[size];
        int i = 0;
        for (Field field : collection)
        {
            result[i++] = field;
        }
        return result;
    }

    public static void invokeFieldValue(Object obj, String fieldName, Object value, Class<?>... type)
    {
        Class<?> clazz = obj.getClass();
        try
        {
            String methodName = "set" + StringUtils.firstToUpper(fieldName);
            invokeMethodValue(obj, methodName, value, type);
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "%s反射Field %s失败， obj = %s", clazz, fieldName, obj);
//			String methodName = "is" + StringUtils.firstToUpper(fieldName);
//			invokeMethodValue(obj, methodName, value);
        }
    }

    public static void invokeMethodValue(Object obj, String methodName, Object value, Class<?>... type)
    {
        Class<?> clazz = obj.getClass();
        try
        {
            Method method = clazz.getMethod(methodName, type);
            method.setAccessible(true);
            method.invoke(obj, value);
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "%s 反射 %s 失败， obj = %s", clazz, methodName, obj);
        }
    }

    public static Object invokeMethod(Object obj, String methodName) throws Exception
    {
        Class<?> clazz = obj.getClass();
        try
        {
            Method method = clazz.getMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (Exception e)
        {
            LOG.warn(Module.COMMON, "%s 反射 %s 失败， obj = %s", clazz, methodName, obj);
            throw e;
        }
    }

    /**
     * 扫面改包路径下所有添加anno的class文件
     *
     * @param packageName
     * @param anno        注解className
     * @return
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> scanPackage(String packageName, String anno) throws ClassNotFoundException
    {
        Class annoClass = Class.forName(anno);
        return scanPackage(packageName, new ClassFilter()
        {
            @Override
            public boolean accept(Class<?> clazz)
            {
                if (clazz.getAnnotation(annoClass) != null)
                {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，</br>
     * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException</br>
     * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @param classFilter class过滤器，过滤掉不需要的class
     * @return
     */
    public static Set<Class<?>> scanPackage(String packageName, ClassFilter classFilter)
    {
        final long startTime = System.currentTimeMillis();
        final Set<Class<?>> classes = new HashSet<>();
        try
        {
            final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
            final String packagePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageName.replace(".", "/") + "/**/*.class";
            final Resource[] resources = resolver.getResources(packagePath);
            final String costTimeStr = DateUtils.getBeforeTimeStr(new Date(System.currentTimeMillis() - startTime));
            LOG.minfo(Module.COMMON, "resolver.getResources complete, packageName = {}, costTime = {}", packageName, costTimeStr);
            if (resources == null || resources.length == 0)
            {
                return new HashSet<>();
            }

            for (Resource resource : resources)
            {
                LOG.debug(Module.COMMON, "Scan classpath: [%s]", resource.getURI().toString());
            }

            final DefaultEventLoopGroup scanPackageGroup = new DefaultEventLoopGroup(12, new DefaultThreadFactory("scanPackage"));
            final CountDownLatch latch = new CountDownLatch(resources.length);
            for (Resource resource : resources)
            {
                scanPackageGroup.execute(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        try
                        {
                            scanPackage(packageName, resource, classFilter, classes);
                        } catch (IOException | ClassNotFoundException e)
                        {
                            LOG.error(Module.COMMON, e, "scan packageName [%s] error, resource = %s", packageName, resource);
                        } finally
                        {
                            latch.countDown();
                        }
                    }
                });
            }
            latch.await();
        } catch (Exception e)
        {
            LOG.error(Module.COMMON, e, "scan packageName [%s] error", packageName);
        }
        final long costTime = System.currentTimeMillis() - startTime;
        final String costTimeStr = DateUtils.getBeforeTimeStr(new Date(costTime));
        LOG.minfo(Module.COMMON, "Scan classpath complete, packageName = %s, costTime = %s", packageName, costTimeStr);

//        if (StringUtils.isNullEmpty(packageName)) throw new NullPointerException("packageName can't be blank!");
//        packageName = getWellFormedPackageName(packageName);
//
//        final Set<Class<?>> classes = new HashSet<Class<?>>();
//        for (String classPath : getClassPaths(packageName)) {
//            LOG.debug(Module.COMMON, "Scan classpath: [%s]", classPath);
//            // 填充 classes
//            fillClasses(classPath, packageName, classFilter, classes);
//        }
//
//        //如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
//        if (classes.isEmpty()) {
//            for (String classPath : getJavaClassPaths()) {
//                LOG.debug(Module.COMMON, "Scan java classpath: [%s]", classPath);
//                // 填充 classes
//                fillClasses(new File(classPath), packageName, classFilter, classes);
//            }
//        }
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//        LOG.info(Module.COMMON, "Scan class: --------------------------------");
//
//        for (Class<?> clazz : classes) {
//            LOG.info(Module.COMMON, "Scan class: [%s]", clazz);
//        }
        return classes;
    }

    private static void scanPackage(String packageName, Resource resource, ClassFilter classFilter, Set<Class<?>> classes) throws IOException, ClassNotFoundException
    {
        final String url = resource.getURI().toString();
        final int start = url.lastIndexOf(packageName.replace(".", "/"));
        final int end = url.lastIndexOf(".class");
        String name = url.substring(start, end);
        name = name.replace("/", ".");
        final Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(name);
        LOG.debug(Module.COMMON, "Find class: [%s]", clazz);

        if (classFilter != null)
        {
            if (classFilter.accept(clazz))
            {
                synchronized (classes)
                {
                    classes.add(clazz);
                }
            }
        } else
        {
            synchronized (classes)
            {
                classes.add(clazz);
            }
        }
    }

    /**
     * 获得指定类中的Public方法名<br>
     * 去重重载的方法
     *
     * @param clazz 类
     */
    public final static Set<String> getMethods(Class<?> clazz)
    {
        HashSet<String> methodSet = new HashSet<String>();
        Method[] methodArray = clazz.getMethods();
        for (Method method : methodArray)
        {
            String methodName = method.getName();
            methodSet.add(methodName);
        }
        return methodSet;
    }

    /**
     * 获得ClassPath
     *
     * @return
     */
    public static Set<String> getClassPathResources()
    {
        return getClassPaths(StringUtils.EMPTY);
    }

    /**
     * 获得ClassPath
     *
     * @param packageName 包名称
     * @return
     */
    public static Set<String> getClassPaths(String packageName)
    {
        String packagePath = packageName.replace(StringUtils.DOT, StringUtils.SLASH);
        Enumeration<URL> resources;
        try
        {
            resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
        } catch (IOException e)
        {
            LOG.error(Module.COMMON, e, "Error when load classPath [%s]", packagePath);
            return null;
        }
        Set<String> paths = new HashSet<>();
        while (resources.hasMoreElements())
        {
            paths.add(resources.nextElement().getPath());
        }
        return paths;
    }

    /**
     * 获得Java ClassPath路径，不包括 jre<br>
     *
     * @return
     */
    public static String[] getJavaClassPaths()
    {
        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        return classPaths;
    }

    /**
     * 转换基本类型
     *
     * @param clazz    转换到的类
     * @param valueStr 被转换的字符串
     * @return 转换后的对象，如果非基本类型，返回null
     */
    public static Object parseBasic(Class<?> clazz, String valueStr)
    {
        switch (BASIC_TYPE.valueOf(clazz.getSimpleName().toUpperCase()))
        {
            case STRING:
                return valueStr;
            case BYTE:
                if (clazz == byte.class)
                {
                    return Byte.parseByte(valueStr);
                }
                return Byte.valueOf(valueStr);
            case SHORT:
                if (clazz == short.class)
                {
                    return Short.parseShort(valueStr);
                }
                return Short.valueOf(valueStr);
            case INT:
                return Integer.parseInt(valueStr);
            case INTEGER:
                return Integer.valueOf(valueStr);
            case LONG:
                if (clazz == long.class)
                {
                    return Long.parseLong(valueStr);
                }
                return Long.valueOf(valueStr);
            case DOUBLE:
                if (clazz == double.class)
                {
                    return Double.parseDouble(valueStr);
                }
            case FLOAT:
                if (clazz == float.class)
                {
                    return Float.parseFloat(valueStr);
                }
                return Float.valueOf(valueStr);
            case BOOLEAN:
                if (clazz == boolean.class)
                {
                    return Boolean.parseBoolean(valueStr);
                }
                return Boolean.valueOf(valueStr);
            case CHAR:
                return valueStr.charAt(0);
            case CHARACTER:
                return Character.valueOf(valueStr.charAt(0));
            default:
                return null;
        }
    }

    /**
     * 转换基本类型
     *
     * @param clazz
     * @return
     */
    public static Class<?> castToPrimitive(Class<?> clazz)
    {
        BASIC_TYPE basicType;
        try
        {
            basicType = BASIC_TYPE.valueOf(clazz.getSimpleName().toUpperCase());
        } catch (Exception e)
        {
            return clazz;
        }
        //基本类型
        switch (basicType)
        {
            case BYTE:
                return byte.class;
            case SHORT:
                return short.class;
            case INT:
                return int.class;
            case LONG:
                return long.class;
            case DOUBLE:
                return double.class;
            case FLOAT:
                return float.class;
            case BOOLEAN:
                return boolean.class;
            case CHAR:
                return char.class;
            default:
                return clazz;
        }
    }

    /**
     * 当前线程的class loader
     *
     * @return
     */
    public static ClassLoader getContextClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获得class loader<br>
     * 若当前线程class loader不存在，取当前类的class loader
     *
     * @return
     */
    public static ClassLoader getClassLoader()
    {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null)
        {
            classLoader = ClassUtil.class.getClassLoader();
        }
        return classLoader;
    }

    //--------------------------------------------------------------------------------------------------- Private method start
    /**
     * 文件过滤器，过滤掉不需要的文件<br>
     * 只保留Class文件、目录和Jar
     */
    private static FileFilter fileFilter = new FileFilter()
    {
        @Override
        public boolean accept(File pathname)
        {
            return isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);
        }
    };

    /**
     * 改变 com -> com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."
     * </br>那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
     *
     * @param packageName
     * @return
     */
    private static String getWellFormedPackageName(String packageName)
    {
        return packageName.lastIndexOf(StringUtils.DOT) != packageName.length() - 1 ? packageName + StringUtils.DOT : packageName;
    }

    /**
     * 填充满足条件的class 填充到 classes<br>
     * 同时会判断给定的路径是否为Jar包内的路径，如果是，则扫描此Jar包
     *
     * @param path        Class文件路径或者所在目录Jar包路径
     * @param packageName 需要扫面的包名
     * @param classFilter class过滤器
     * @param classes     List 集合
     */
    private static void fillClasses(String path, String packageName, ClassFilter classFilter, Set<Class<?>> classes)
    {
        //判定给定的路径是否为
        int index = path.lastIndexOf(JAR_PATH_EXT);
        if (index != -1)
        {
            path = path.substring(0, index + JAR_FILE_EXT.length());
            path = StringUtils.removePrefix(path, PATH_FILE_PRE);
            processJarFile(new File(path), packageName, classFilter, classes);
        } else
        {
            fillClasses(new File(path), packageName, classFilter, classes);
        }
    }

    /**
     * 填充满足条件的class 填充到 classes
     *
     * @param file        Class文件或者所在目录Jar包文件
     * @param packageName 需要扫面的包名
     * @param classFilter class过滤器
     * @param classes     List 集合
     */
    private static void fillClasses(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes)
    {
        if (file.isDirectory())
        {
            processDirectory(file, packageName, classFilter, classes);
        } else if (isClassFile(file))
        {
            processClassFile(file, packageName, classFilter, classes);
        } else if (isJarFile(file))
        {
            processJarFile(file, packageName, classFilter, classes);
        }
    }

    /**
     * 处理如果为目录的情况,需要递归调用 fillClasses方法
     *
     * @param directory
     * @param packageName
     * @param classFilter
     * @param classes
     */
    private static void processDirectory(File directory, String packageName, ClassFilter classFilter, Set<Class<?>> classes)
    {
        for (File file : directory.listFiles(fileFilter))
        {
            fillClasses(file, packageName, classFilter, classes);
        }
    }

    /**
     * 处理为class文件的情况,填充满足条件的class 到 classes
     *
     * @param file
     * @param packageName
     * @param classFilter
     * @param classes
     */
    private static void processClassFile(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes)
    {
        final String filePathWithDot = file.getAbsolutePath().replace(File.separator, StringUtils.DOT);
        int subIndex = -1;
        if ((subIndex = filePathWithDot.indexOf(packageName)) != -1)
        {
            final String className = filePathWithDot.substring(subIndex).replace(CLASS_EXT, StringUtils.EMPTY);
            fillClass(className, packageName, classes, classFilter);
        }
    }

    /**
     * 处理为jar文件的情况，填充满足条件的class 到 classes
     *
     * @param file
     * @param packageName
     * @param classFilter
     * @param classes
     */
    private static void processJarFile(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes)
    {
        try
        {
            if (file.exists())
            {
                for (JarEntry entry : Collections.list(new JarFile(file).entries()))
                {
                    if (isClass(entry.getName()))
                    {
                        final String className = entry.getName().replace(StringUtils.SLASH, StringUtils.DOT).replace(CLASS_EXT, StringUtils.EMPTY);
                        fillClass(className, packageName, classes, classFilter);
                    }
                }
            }
        } catch (Throwable ex)
        {
            LOG.error(Module.COMMON, ex, "Error when process jar file [%s]", file);
        }
    }

    /**
     * 填充class 到 classes
     *
     * @param className
     * @param packageName
     * @param classes
     * @param classFilter
     */
    private static void fillClass(String className, String packageName, Set<Class<?>> classes, ClassFilter classFilter)
    {
        if (className.startsWith(packageName))
        {
            try
            {
                final Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                if (classFilter == NULL_CLASS_FILTER || classFilter.accept(clazz))
                {
                    classes.add(clazz);
                }
            } catch (Throwable ex)
            {
                LOG.error(Module.COMMON, ex, "Error when fill Class [%s]", className);
            }
        }
    }

    private static boolean isClassFile(File file)
    {
        return isClass(file.getName());
    }

    private static boolean isClass(String fileName)
    {
        return fileName.endsWith(CLASS_EXT);
    }

    private static boolean isJarFile(File file)
    {
        return file.getName().contains(JAR_FILE_EXT);
    }

    /**
     * 类过滤器，用于过滤不需要加载的类
     */
    public interface ClassFilter
    {
        boolean accept(Class<?> clazz);
    }

    private static final List<String> BASIC_TYPE_LIST = Arrays.asList(
            String.class.getName(),
            Long.class.getName(),
            Integer.class.getName(),
            Short.class.getName(),
            Double.class.getName(),
            Float.class.getName(),
            Boolean.class.getName(),
            BigDecimal.class.getName(),
            Date.class.getName(),
            LocalDateTime.class.getName(),
            LocalDate.class.getName(),
            LocalTime.class.getName()
    );

    public static boolean isBasicType(Field field)
    {
        final String innerClassName = getListInnerClassName(field);
        final boolean isBasicType = BASIC_TYPE_LIST.contains(innerClassName);
        return isBasicType;
    }

    public static String getListInnerClassName(Field field)
    {
        final String genericTypeName = field.getGenericType().getTypeName();
        final String innerClassName = genericTypeName.substring(genericTypeName.indexOf("<") + 1, genericTypeName.indexOf(">"));
        return innerClassName;
    }
}