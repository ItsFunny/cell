//package org.slf4j.impl;
//
//import com.cell.base.core.hooks.IHook;
//import jdk.internal.dynalink.beans.StaticClass;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Map;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-07-21 21:41
// */
//public class ProxyClassLoader extends ClassLoader
//{
//    private Map<String, IHook<Class<?>>> targets = null;
//
//    public ProxyClassLoader(Map<String, IHook<Class<?>>> targets, ClassLoader parent)
//    {
//        super(parent);
//        this.targets = targets;
//    }
//
//    @Override
//    public Class<?> loadClass(String name) throws ClassNotFoundException
//    {
////        if (targets.containsKey(name))
////        {
////            return this.findClass(name);
////        }
//        return super.loadClass(name);
//    }
//
//
//    private byte[] loadClassData(String name) throws IOException
//    {
//
//        InputStream stream = getClass().getClassLoader().getResourceAsStream(
//                name);
//        int size = stream.available();
//        byte buff[] = new byte[size];
//        DataInputStream in = new DataInputStream(stream);
//        // Reading the binary data
//        in.readFully(buff);
//        in.close();
//        return buff;
//    }
//
//
//    @Override
//    protected Class<?> findClass(String name) throws ClassNotFoundException
//    {
//        String file = name.replace('.', File.separatorChar) + ".class";
//        byte[] byteArr = null;
//        try
//        {
//            byteArr = this.loadClassData(file);
//            Class<?> binder = defineClass(name, byteArr, 0, byteArr.length);
//            resolveClass(binder);
//            this.targets.get(name).hook(binder);
//            return binder;
//        } catch (IOException e)
//        {
//            throw new ClassNotFoundException(name);
//        }
//    }
//
//    public static void main(String[] args) throws Exception
//    {
//        ProxyClassLoader loader = new ProxyClassLoader(null, ProxyClassLoader.class.getClassLoader());
//        Class<?> aClass = loader.loadClass(StaticLoggerBinder.class.getName());
//        System.out.println(aClass);
//    }
//}
