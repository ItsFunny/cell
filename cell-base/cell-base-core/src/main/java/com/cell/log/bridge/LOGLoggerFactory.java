//package com.cell.log.bridge;
//
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.util.ContextInitializer;
//import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
//import ch.qos.logback.core.status.StatusUtil;
//import ch.qos.logback.core.util.StatusPrinter;
//import com.cell.config.AbstractInitOnce;
//import com.cell.context.InitCTX;
//import com.cell.models.Module;
//import org.slf4j.ILoggerFactory;
//import org.slf4j.Logger;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class LOGLoggerFactory extends AbstractInitOnce implements ILoggerFactory
//{
//    private static final LOGLoggerWrapper COMMON_LOGGER_WRAPPER = new LOGLoggerWrapper(Module.THIRD_PARTY);
//    private static Map<String, LOGLoggerWrapper> logWrapperMap = new HashMap<>();
//    private LoggerContext defaultLoggerContext = new LoggerContext();
//    private final ContextSelectorStaticBinder contextSelectorBinder = ContextSelectorStaticBinder.getSingleton();
//    private static Object KEY = new Object();
//
//    static
//    {
//        for (Map.Entry<String, Module> entry : ClassBridge.moduleMap.entrySet())
//        {
//            logWrapperMap.put(entry.getKey(), new LOGLoggerWrapper(entry.getValue()));
//        }
//    }
//
//    @Override
//    public Logger getLogger(String name)
//    {
//        for (Map.Entry<String, LOGLoggerWrapper> entry : logWrapperMap.entrySet())
//        {
//            if (name.startsWith(entry.getKey()))
//            {
//                return entry.getValue();
//            }
//        }
//        return COMMON_LOGGER_WRAPPER;
//    }
//
////    @Override
////    protected InternalLogger newInstance(String name)
////    {
////        for (Map.Entry<String, LOGLoggerWrapper> entry : logWrapperMap.entrySet())
////        {
////            if (name.startsWith(entry.getKey()))
////            {
////                return entry.getValue();
////            }
////        }
////        return COMMON_LOGGER_WRAPPER;
////    }
//
//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//        try
//        {
//            (new ContextInitializer(this.defaultLoggerContext)).autoConfig();
//            if (!StatusUtil.contextHasStatusListener(this.defaultLoggerContext))
//            {
//                StatusPrinter.printInCaseOfErrorsOrWarnings(this.defaultLoggerContext);
//            }
//            this.contextSelectorBinder.init(this.defaultLoggerContext, KEY);
//        } catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//}
