//package com.cell.log.factory;
//
//import ch.qos.logback.classic.LoggerContext;
//import com.cell.log.LoggerProxy;
//import com.cell.log.wrapper.CellLogProxy;
//import org.slf4j.ILoggerFactory;
//import org.slf4j.Logger;
//import sun.util.logging.LoggingProxy;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-07-07 22:10
// */
//public class CellLogFactoryContext implements ILoggerFactory
//{
//    private LoggerContext context;
//    private Set<String> flagLogger = new HashSet<>();
//
//    public CellLogFactoryContext(LoggerContext context)
//    {
//        this.context = context;
//    }
//
//    @Override
//    public Logger getLogger(String s)
//    {
//        synchronized (this){
//            ch.qos.logback.classic.Logger logger = context.getLogger(s);
//            if (!this.flagLogger.contains(s)){
//
//            }
//        }
//
//        CellLogProxy proxy = new CellLogProxy(logger);
//        return proxy;
//    }
//
//    public LoggerProxy getProxyLogger(String s)
//    {
//        return context.getLogger(s);
//    }
//}
