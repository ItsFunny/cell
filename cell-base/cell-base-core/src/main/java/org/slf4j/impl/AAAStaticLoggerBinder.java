//package org.slf4j.impl;
//
//import ch.qos.logback.classic.LoggerContext;
//import com.cell.hooks.IHook;
//import com.cell.log.factory.DefaultSlf4jLoggerFactory;
//import lombok.Data;
//import org.slf4j.ILoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.helpers.Util;
//import org.slf4j.spi.LoggerFactoryBinder;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Data
//public class StaticLoggerBinder implements LoggerFactoryBinder
//{
//    private static final StaticLoggerBinder BINDER = new StaticLoggerBinder();
//
//
//    @Override
//    public ILoggerFactory getLoggerFactory()
//    {
//        return DefaultSlf4jLoggerFactory.getInstance();
//    }
//
//    @Override
//    public String getLoggerFactoryClassStr()
//    {
//        return DefaultSlf4jLoggerFactory.getInstance().getClass().getName();
//    }
//
//
//    public static final StaticLoggerBinder getSingleton()
//    {
//        return BINDER;
//    }
//
//    static
//    {
//        BINDER.init();
//    }
//
//
//    void init()
//    {
//        Map<String, IHook<Class<?>>> targets = new HashMap<>();
////        targets.put(StaticLoggerBinder.class.getName(), (c) ->
////        {
////            try
////            {
////                Field field = c.getDeclaredField("SINGLETON");
////                Field modifiersField = Field.class.getDeclaredField("modifiers");
////                modifiersField.setAccessible(true);
////                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//////                ClassLoader classLoader = StaticLoggerBinder.class.getClassLoader();
//////                ProxyStaticLogBinder proxyStaticLogBinder = new ProxyStaticLogBinder();
//////                LoggerFactoryBinder proxy = (LoggerFactoryBinder) Proxy.newProxyInstance(classLoader, new Class[]{LoggerFactoryBinder.class}, proxyStaticLogBinder);
////                field.setAccessible(true);
////                field.set(null, BINDER);
////            } catch (Exception e)
////            {
////                e.printStackTrace();
////            }
////        });
//
//        try
//        {
//            try
//            {
//                DefaultSlf4jLoggerFactory.getInstance().initOnce(null);
//            } catch (Exception var2)
//            {
//                Util.report("Failed to auto configure default logger context", var2);
//            }
//        } catch (Exception var3)
//        {
//            Util.report("Failed to instantiate [" + LoggerContext.class.getName() + "]", var3);
//        }
//    }
//
//    private StaticLoggerBinder binder;
//
//
//    public static void main(String[] args) throws Exception
//    {
////        ClassLoader classLoader = StaticLoggerBinder.class.getClassLoader();
////        CellStaticLoggerBinder binder = new CellStaticLoggerBinder();
////        binder.setBinder(StaticLoggerBinder.getSingleton());
////        LoggerFactoryBinder proxy = (LoggerFactoryBinder) Proxy.newProxyInstance(classLoader, new Class[]{LoggerFactoryBinder.class}, binder);
////        ILoggerFactory loggerFactory = proxy.getLoggerFactory();
////        System.out.println(loggerFactory);
//        Logger logger = LoggerFactory.getLogger("xxxxx");
//        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
//        System.out.println(logger);
//
//        TimeUnit.SECONDS.sleep(20);
//    }
//}
