package com.cell.utils;

import com.cell.constants.BitConstants;
import com.cell.wrapper.MonoWrapper;
import org.junit.platform.commons.util.AnnotationUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-12 05:36
 */
public class ReflectionUtils extends org.reflections.ReflectionUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    private static final String DEFAULT_PREFIX = "com.cell";


    public static Mono<MonoWrapper<Boolean>> containAnnotaitonsInFieldOrMethod(Class<?> clz, byte bitConstants, Class<? extends Annotation>... ac)
    {
        if (ac.length == 1)
        {
            MonoWrapper<Boolean> wp = new MonoWrapper<>();
            wp.setRet(containAnnotaitonsInFieldOrMethod(clz, ac[0]));
            return Mono.just(wp);
        }
        Flux<Class<? extends Annotation>> classFlux = Flux.fromStream(Stream.of(ac));
        MonoWrapper<Boolean> wp = new MonoWrapper<>();
        wp.setRet(false);
        return classFlux.handle((anno, sink) ->
        {
            if (containAnnotaitonsInFieldOrMethod(clz, anno))
            {
                if (bitConstants == BitConstants.or)
                {
                    wp.setRet(true);
                    sink.complete();
                    return;
                }
            } else
            {
                if (bitConstants == BitConstants.and)
                {
                    wp.setRet(false);
                    sink.complete();
                    return;
                }
            }
            sink.next(anno);
        }).then().thenReturn(wp);
    }

    // FIXME ,RETURN mono instead of boolean
    public static boolean containAnnotaitonsInFieldOrMethod(Class<?> clz, Class<? extends Annotation> ac)
    {
        return CollectionUtils.isNotEmpty(AnnotationUtils.findAnnotatedFields(clz, ac, (c) -> true)) || (CollectionUtils.isNotEmpty(AnnotationUtils.findAnnotatedMethods(clz, ac, org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)));
    }

    /**
     * Gets all genesis class by interface.
     * 获取泛型接口的所有实现
     *
     * @param prefix         the prefix 扫描包的前缀
     * @param interfaceClazz the interface clazz 接口名称
     * @param genesisClazz   the genesis clazz 还会有一个泛型类型判断
     * @return the all genesis class by interface
     */
    public static List<Class> getAllGenesisClassByInterface(String prefix, Class interfaceClazz, Class
            genesisClazz, ScanTillSatisfiedClazzHandler handler)
    {
        List<Class> result = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(prefix))
        {
            prefix = DEFAULT_PREFIX;
        }
        Reflections reflections = new Reflections(prefix);

        Set<Class<?>> subTypesOf = reflections.getSubTypesOf(interfaceClazz);
        for (Class<?> c : subTypesOf)
        {
            // 获取这个类所实现的所有接口
            Type[] genericInterfaces = c.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces)
            {
                boolean isBreak = false;
                // 获取这个类在这个接口上的的泛型信息
                ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) genericInterface;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments)
                {
                    // 转换为class
                    Class actualClass = (Class) actualTypeArgument;
                    // 匹配是否是这个子类或者是能强转的
                    if (actualClass.isAssignableFrom(genesisClazz))
                    {
                        if (handler.handle(c))
                        {
                            result.add(c);
                        }
                        isBreak = true;
                        break;
                    }
                }
                if (isBreak)
                {
                    break;
                }
            }
        }

//        subTypesOf.stream().forEach(c->{
//            Type[] genericInterfaces = c.getGenericInterfaces();
//            Arrays.stream(genericInterfaces).forEach(g->{
//                ParameterizedTypeImpl parameterizedType= (ParameterizedTypeImpl) g;
//                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//                Arrays.stream(actualTypeArguments).forEach(t->{
//
//                });
//            });
//        });

        return result;
    }

    public static List<Class> getAllGenesisClassByInterface(Class interfaceClazz, Class
            genesisClazz, ScanTillSatisfiedClazzHandler handler)
    {
        return getAllGenesisClassByInterface(DEFAULT_PREFIX, interfaceClazz, genesisClazz, handler);
    }

    public static interface ScanTillSatisfiedClazzHandler
    {
        boolean handle(Class<?> clazz);
    }

    public static ArrayList<Class> getAllClassByInterface(Class clazz)
    {
        ArrayList<Class> list = new ArrayList<>();
        // 判断是否是一个接口
        if (clazz.isInterface())
        {
            try
            {
                ArrayList<Class> allClass = getAllClass(clazz.getPackage().getName());
                /**
                 * 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己
                 */
                for (int i = 0; i < allClass.size(); i++)
                {
                    /**
                     * 判断是不是同一个接口
                     */
                    // isAssignableFrom:判定此 Class 对象所表示的类或接口与指定的 Class
                    // 参数所表示的类或接口是否相同，或是否是其超类或超接口
                    if (clazz.isAssignableFrom(allClass.get(i)))
                    {
                        if (!clazz.equals(allClass.get(i)))
                        {
                            // 自身并不加进去
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e)
            {
                LOG.error("出现异常{}", e.getMessage());
                throw new RuntimeException("出现异常" + e.getMessage());
            }
        }
        LOG.info("class list size :" + list.size());
        return list;
    }


    /**
     * 从一个指定路径下查找所有的类
     *
     * @param packagename
     */
    private static ArrayList<Class> getAllClass(String packagename)
    {


        LOG.info("packageName to search：" + packagename);
        List<String> classNameList = getClassName(packagename);
        ArrayList<Class> list = new ArrayList<>();

        for (String className : classNameList)
        {
            try
            {
                list.add(Class.forName(className));
            } catch (ClassNotFoundException e)
            {
                LOG.error("load class from name failed:" + className + e.getMessage());
                throw new RuntimeException("load class from name failed:" + className + e.getMessage());
            }
        }
        LOG.info("find list size :" + list.size());
        return list;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName)
    {

        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null)
        {
            String type = url.getProtocol();
            LOG.debug("file type : " + type);
            if (type.equals("file"))
            {
                String fileSearchPath = url.getPath();
                LOG.debug("fileSearchPath: " + fileSearchPath);
                fileSearchPath = fileSearchPath.substring(0, fileSearchPath.indexOf("/classes"));
                LOG.debug("fileSearchPath: " + fileSearchPath);
                fileNames = getClassNameByFile(fileSearchPath);
            } else if (type.equals("jar"))
            {
                try
                {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    fileNames = getClassNameByJar(jarFile, packagePath);
                } catch (java.io.IOException e)
                {
                    throw new RuntimeException("open Package URL failed：" + e.getMessage());
                }

            } else
            {
                throw new RuntimeException("file system not support! cannot load MsgProcessor！");
            }
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(String filePath)
    {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles)
        {
            if (childFile.isDirectory())
            {
                myClassName.addAll(getClassNameByFile(childFile.getPath()));
            } else
            {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class"))
                {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(JarFile jarFile, String packagePath)
    {
        List<String> myClassName = new ArrayList<String>();
        try
        {
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements())
            {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                //LOG.info("entrys jarfile:"+entryName);
                if (entryName.endsWith(".class"))
                {
                    entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                    myClassName.add(entryName);
                    //LOG.debug("Find Class :"+entryName);
                }
            }
        } catch (Exception e)
        {
            LOG.error("发生异常:" + e.getMessage());
            throw new RuntimeException("发生异常:" + e.getMessage());
        }
        return myClassName;
    }


    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getInterfaceT(Object o, int index)
    {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }


    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index)
    {
        Type type = o.getClass().getGenericSuperclass();
        return getaClass(index, type);
    }

    private static Class<?> getaClass(int index, Type type)
    {
        if (type instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else
        {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    private static Class<?> checkType(Type type, int index)
    {
        if (type instanceof Class<?>)
        {
            return (Class<?>) type;
        } else
        {
            return getaClass(index, type);
        }
    }

    public class MyTypeToken<T>
    {
        private Type type;
        private Type parameterizedType;

        public MyTypeToken()
        {
            getClassT(this, 0);
        }

        /**
         * 获取类上的泛型T
         *
         * @param o     接口
         * @param index 泛型索引
         */
        public Class<?> getClassT(Object o, int index)
        {
            Type type = o.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType)
            {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type actType = parameterizedType.getActualTypeArguments()[index];
                return checkType(actType, index);
            } else
            {
                String className = type == null ? "null" : type.getClass().getName();
                throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                        + ", but <" + type + "> is of type " + className);
            }
        }

        private Class<?> checkType(Type type, int index)
        {
            if (type instanceof Class<?>)
            {
                this.type = type;
                return (Class<?>) type;
            } else if (type instanceof ParameterizedType)
            {
                ParameterizedType pt = (ParameterizedType) type;
                Type t = pt.getActualTypeArguments()[index];
                parameterizedType = pt;
                return checkType(t, index);
            } else
            {
                String className = type == null ? "null" : type.getClass().getName();
                throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                        + ", but <" + type + "> is of type " + className);
            }
        }

        public Type getType()
        {
            return type;
        }

        public Type getParameterizedType()
        {
            return parameterizedType;
        }
    }

    public static boolean matchClazAllGenesic(Class<?> clz, Type match)
    {
        if (clz.equals(Object.class))
        {
            return false;
        }
        Type genericSuperclass = clz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType))
        {
            clz = clz.getSuperclass();
            return matchClazAllGenesic(clz, match);
        }
        ParameterizedType pType = (ParameterizedType) genericSuperclass;
        boolean ret = Arrays.asList(pType.getActualTypeArguments()).contains(match);
        if (!ret)
        {
            clz = clz.getSuperclass();
            return matchClazAllGenesic(clz, match);
        }
        return true;
    }

    // 获取某个class 上的接口是否有某个targetClz的接口泛型
    public static Type getClzGenesicInterfaceTill(Class<?> clz, Class<?> targetClz)
    {
        if (clz.equals(Object.class))
        {
            throw new RuntimeException("asd");
        }
        if (clz.equals(targetClz))
        {
            // FIXME ,ERROR
            return clz;
        }
        Type[] genericInterfaces = clz.getGenericInterfaces();
        Optional<Type> first = Stream.of(genericInterfaces).filter(p -> !p.getClass().equals(targetClz) || !(p instanceof ParameterizedType)).findFirst();
        return first.orElseGet(() -> getClzGenesicInterfaceTill(clz.getSuperclass(), targetClz));
    }
}
