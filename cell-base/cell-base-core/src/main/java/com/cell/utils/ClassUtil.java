package com.cell.utils;


import com.cell.annotations.CellOrder;
import com.cell.concurrent.base.DefaultThreadFactory;
import com.cell.constants.Constants;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.google.common.base.Stopwatch;
import io.netty.channel.DefaultEventLoopGroup;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

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
                LOG.warning(Module.COMMON, e, "{}反射Field {}失败， obj = {}", clazz, fieldName, obj);
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
//    			LOG.warning(Module.COMMON, "{}反射Field {}失败， obj = {}", clazz, fieldName, obj);
                return null;
            }
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "{}反射Field {}失败， obj = {}", clazz, fieldName, obj);
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
//    			LOG.warning(Module.COMMON, "{}反射Field {}失败， obj = {}", clazz, fieldName, obj);
                return null;
            }
            field.setAccessible(true);
            return field;
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "{}反射Field {}失败， obj = {}", clazz, fieldName, obj);
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

    public static Object invokeFieldValue(Object obj, String fieldName, Object value)
    {
        Class<?> clazz = obj.getClass();
        try
        {
            String methodName = "set" + StringUtils.firstToUpper(fieldName);
            return invokeMethodValue(obj, methodName, value);
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "{}反射Field {}失败， obj = {}", clazz, fieldName, obj);
//			String methodName = "is" + StringUtils.firstToUpper(fieldName);
//			invokeMethodValue(obj, methodName, value);
            return null;
        }
    }

    public static Object invokeMethodValue(Object obj, String methodName, Object value)
    {
        return invokeMethodValue(obj, methodName, Arrays.asList(value));
    }


    public static Object invokeMethodValue(Object obj, String methodName, List<Object> values)
    {
        Class<?> clazz = obj.getClass();
        List<? extends Class<?>> types = values.stream().map(p -> p.getClass()).collect(Collectors.toList());
        Object[] data = values.toArray(new Object[values.size()]);
        try
        {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods)
            {
                System.out.println(method.getName());
            }
            Method method = clazz.getDeclaredMethod(methodName, types.toArray(new Class[types.size()]));
            method.setAccessible(true);
            return method.invoke(obj, data);
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "{} 反射 {} 失败， obj = {}", clazz, methodName, obj);
            return null;
        }
    }


    public static void mustInvokeMethodValue(Object obj, String methodName, List<Object> values, Class<?>... type)
    {
        Class<?> clazz = obj.getClass();
        try
        {
            Method method = clazz.getDeclaredMethod(methodName, type);
            method.setAccessible(true);
            method.invoke(obj, values.toArray());
        } catch (Exception e)
        {
            LOG.warning(Module.COMMON, e, "{} 反射 {} 失败， obj = {}", clazz, methodName, obj);
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
            LOG.warn(Module.COMMON, "{} 反射 {} 失败， obj = {}", clazz, methodName, obj);
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
        Stopwatch wa = Stopwatch.createStarted();
        final Set<Class<?>> classes = new HashSet<>();
        try
        {

            final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
            final String packagePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageName.replace(".", "/") + "/**/*.class";

            final Resource[] resources = resolver.getResources(packagePath);
            long elapsed = wa.elapsed(TimeUnit.SECONDS);
            wa.reset();
            wa.start();
            LOG.minfo(Module.COMMON, "resolver.getResources complete, packageName = {}, costTime = {}", packageName, elapsed);
            if (resources == null || resources.length == 0)
            {
                return new HashSet<>();
            }

            for (Resource resource : resources)
            {
                LOG.debug(Module.COMMON, "Scan classpath: [{}]", resource.getURI().toString());
            }

            final DefaultEventLoopGroup scanPackageGroup = new DefaultEventLoopGroup(12, new DefaultThreadFactory("scanPackage"));
            final CountDownLatch latch = new CountDownLatch(resources.length);
            for (Resource resource : resources)
            {
//                try
//                {
//                    scanPackage(packageName, resource, classFilter, classes);
//                } catch (IOException | ClassNotFoundException e)
//                {
//                    LOG.error(Module.COMMON, e, "scan packageName [{}] error, resource = {}", packageName, resource);
//                } finally
//                {
//                    latch.countDown();
//                }
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
                            LOG.error(Module.COMMON, e, "scan packageName [{}] error, resource = {}", packageName, resource);
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
            LOG.error(Module.COMMON, e, "scan packageName [{}] error", packageName);
        }
        wa.stop();
        LOG.minfo(Module.COMMON, "Scan classpath complete, packageName = {}, costTime = {}", packageName, wa.elapsed(TimeUnit.SECONDS));

//        if (StringUtils.isNullEmpty(packageName)) throw new NullPointerException("packageName can't be blank!");
//        packageName = getWellFormedPackageName(packageName);
//
//        final Set<Class<?>> classes = new HashSet<Class<?>>();
//        for (String classPath : getClassPaths(packageName)) {
//            LOG.debug(Module.COMMON, "Scan classpath: [{}]", classPath);
//            // 填充 classes
//            fillClasses(classPath, packageName, classFilter, classes);
//        }
//
//        //如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
//        if (classes.isEmpty()) {
//            for (String classPath : getJavaClassPaths()) {
//                LOG.debug(Module.COMMON, "Scan java classpath: [{}]", classPath);
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
//            LOG.info(Module.COMMON, "Scan class: [{}]", clazz);
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
        LOG.debug(Module.COMMON, "Find class: [{}]", clazz);

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
            LOG.error(Module.COMMON, e, "Error when load classPath [{}]", packagePath);
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
            LOG.error(Module.COMMON, ex, "Error when process jar file [{}]", file);
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
                LOG.error(Module.COMMON, ex, "Error when fill Class [{}]", className);
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

    public static int ordererCompare(Class<?> clz1, Class<?> clz2)
    {
        CellOrder anno1 = getMergedAnnotation(clz1, CellOrder.class);
        CellOrder anno2 = getMergedAnnotation(clz2, CellOrder.class);
        int value1 = anno1 != null ? anno1.value() : Constants.DEFAULT_ORDER;
        int value2 = anno2 != null ? anno2.value() : Constants.DEFAULT_ORDER;
        return Integer.compare(value1, value2);
    }

    public static Annotation mustGetAnnotation(Class<?> clz, Class<? extends Annotation> a)
    {
        Annotation annotation = getMergedAnnotation(clz, a);
        if (null == annotation)
        {
            throw new ProgramaException("as");
        }
        return annotation;
    }

    public static Annotation getAnnotation(Class<?> clz, Class<? extends Annotation> a)
    {
        return getMergedAnnotation(clz, a);
    }

    public static boolean hasAnnotation(Class<?> clz, Class<? extends Annotation> a)
    {
        return getAnnotation(clz, a) != null;
    }

    public static boolean hasAnnotation(Object o, Class<? extends Annotation> a)
    {
        return o.getClass().getAnnotation(a) != null;
    }

    public static Class<?> getMainApplicationClass()
    {
        try
        {
            StackTraceElement[] stackTrace = (new RuntimeException()).getStackTrace();
            StackTraceElement[] var2 = stackTrace;
            int var3 = stackTrace.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                StackTraceElement stackTraceElement = var2[var4];
                if ("main".equals(stackTraceElement.getMethodName()))
                {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException e)
        {
            throw new ProgramaException(e);
        }
        throw new ProgramaException("asd");
    }

    public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType)
    {
        return AnnotatedElementUtils.getMergedAnnotationAttributes(element, annotationType);
    }


    public static <T extends Annotation> T getMergedAnnotation(AnnotatedElement element, Class<T> ac)
    {
        return AnnotatedElementUtils.getMergedAnnotation(element, ac);
    }
//    public static void addAnnotationIfNotExist(Class<?> clz, Annotation annotationType)
//    {
//        AnnotationAttributes mergedAnnotationAttributes = getMergedAnnotationAttributes(clz, annotationType.getClass());
//        if (mergedAnnotationAttributes != null && !mergedAnnotationAttributes.isEmpty())
//        {
//            return;
//        }
//        new ByteBuddy().redefine(clz).annotateType()
//    }

    public static boolean checkIsAbstract(Class<?> c)
    {
        Class a;
        try
        {
            a = Class.forName(c.getName());
        } catch (ClassNotFoundException e)
        {
            return false;
        }
        return Modifier.isAbstract(a.getModifiers());
    }
}