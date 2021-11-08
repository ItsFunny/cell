package com.cell.log;

import com.cell.base.common.models.Module;
import com.cell.base.common.models.ModuleInterface;
import com.cell.context.InitCTX;
import com.cell.log.bridge.ACLoggingFactory;
import com.cell.log.config.Log4j2ConfigBuilder;
import com.cell.log.factory.DefaultSlf4jLoggerFactory;
import com.cell.log.impl.DefaultCellLogger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-04 17:39
 */
public class LOG
{
    private static final Object CONFIGURE_LOCK = new Object();
    private static String logFileName = null;
    private static final DefaultCellLogger cellLogger = new DefaultCellLogger();
    public static final LogTypeEnums DEFAULT_LOG_TYPE = LogTypeEnums.NORMAL;
    private static final AtomicBoolean inited = new AtomicBoolean();

    static
    {
        DefaultSlf4jLoggerFactory.getInstance();
        refresh();
    }

    public static void refresh()
    {
        try
        {
            if (inited.get())
            {
                return;
            }
            synchronized (LOG.class)
            {
                if (inited.get())
                {
                    return;
                }
                InitCTX ctx = new InitCTX();
                cellLogger.initOnce(ctx);
                Set<URL> slf4jBindings = getPossibleSLF4JStaticBindings();
                if (slf4jBindings.size() != 1)
                {
                    LOG.error(Module.COMMON, null,
                            "more than one or no slf4j static bindings were found, third party logs may not be logged.");
                    for (URL url : slf4jBindings)
                    {
                        LOG.error(Module.COMMON, null, "binding: {}", url);
                    }
                }

                System.setProperty("org.apache.commons.logging.LogFactory", ACLoggingFactory.class.getName());
                System.setProperty("org.springframework.boot.logging.LoggingSystem", "none");
                inited.set(true);
            }

        } catch (Throwable e)
        {
            e.printStackTrace();
            System.out.println(e);
            LOG.error(Module.LOG, e, "初始化失败");
        }
    }

    public static void setLogLevel(LogLevel logLevel)
    {
        cellLogger.setLogLevel(logLevel);
    }

    private static final String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    public static void trace(String msg, Object... data)
    {
        trace(null, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void minfo(ModuleInterface module, String msg, Object... data)
    {
        info(module, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void info(String msg, Object... data)
    {
        info(null, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void info(ModuleInterface module, String msg, LogTypeEnums en, Object... data)
    {
        info(module, null, en, msg, data);
    }

    public static void debug(String msg, Object... data)
    {
        debug(null, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void warn(String msg, Object... data)
    {
        warn(null, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void warning(ModuleInterface module, Throwable error, String msg, Object... data)
    {
        warn(module, error, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void debug(ModuleInterface module, Error error, String msg, Object... data)
    {
        debug(module, error, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void info(ModuleInterface module, Error error, String msg, Object... data)
    {
        info(module, error, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void info(ModuleInterface module, String msg, Object... data)
    {
        info(module, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void error(ModuleInterface module, Error error, String msg, Object... data)
    {
        error(module, error, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void erroring(ModuleInterface module, String msg, Object... data)
    {
        error(module, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void warn(ModuleInterface module, String msg, Object... data)
    {
        warn(module, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void debug(ModuleInterface module, String msg, Object... data)
    {
        debug(module, null, DEFAULT_LOG_TYPE, msg, data);
    }

    public static void error(String msg, Object... data)
    {
        error(null, null, DEFAULT_LOG_TYPE, msg, data);
    }


    private static final Set<URL> getPossibleSLF4JStaticBindings()
    {
        Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>();
        try
        {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> paths;
            if (loggerFactoryClassLoader == null)
            {
                paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
            } else
            {
                paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            }
            while (paths.hasMoreElements())
            {
                URL path = paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        } catch (IOException e)
        {
            LOG.warn("Error getting resources from path {}", STATIC_LOGGER_BINDER_PATH);
        }
        return staticLoggerBinderPathSet;
    }

//    private static void reflectChangeLog() throws Exception
//    {
//        Field field = StaticLoggerBinder.class.getField("defaultLoggerContext");
//        field.setAccessible(true);
//    }

//    public static void info(Module module, String format, Object... data)
//    {
//        info(module, null, DEFAULT_LOG_TYPE, format, data);
//    }

    //    public static void error(Module module, String format, Object... data)
//    {
//        error(module, null, DEFAULT_LOG_TYPE, format, data);
//    }
    public static void error(ModuleInterface module, Throwable throwable, String format, Object... data)
    {
        error(module, throwable, DEFAULT_LOG_TYPE, format, data);
    }

//    public static void debug(Module module, String format, Object... data)
//    {
//        debug(module, null, DEFAULT_LOG_TYPE, format, data);
//    }

//    public static void warn(Module module, String format, Object... data)
//    {
//        warn(module, null, DEFAULT_LOG_TYPE, format, data);
//    }

    public static void info(ModuleInterface module, Throwable throwable, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        cellLogger.log(module, LogLevel.INFO, logTypeEnums, throwable, format, data);
    }

    public static void info(ModuleInterface module, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        info(module, null, logTypeEnums, format, data);
    }

    public static void debug(ModuleInterface module, Throwable throwable, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        cellLogger.log(module, LogLevel.DEBUG, logTypeEnums, throwable, format, data);
    }

    public static void debug(Module module, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        debug(module, null, logTypeEnums, format, data);
    }

    public static void error(ModuleInterface module, Throwable throwable, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        cellLogger.log(module, LogLevel.ERROR, logTypeEnums, throwable, format, data);
    }

    public static void warn(ModuleInterface module, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        warn(module, null, logTypeEnums, format, data);
    }

    public static void trace(ModuleInterface module, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        trace(module, null, logTypeEnums, format, data);
    }

    public static void warn(ModuleInterface module, Throwable throwable, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        cellLogger.log(module, LogLevel.WARN, logTypeEnums, throwable, format, data);
    }

    public static void trace(ModuleInterface module, Throwable throwable, LogTypeEnums logTypeEnums, String format, Object... data)
    {
        cellLogger.log(module, LogLevel.TRACE, logTypeEnums, throwable, format, data);
    }

//    private static void refreshLog4j(List<String> newAbsFolder, String logFileName)
//    {
//        try
//        {
//            File fakeLog4jConfig = File.createTempFile("log4j", ".tmp");
//            FileWriter fileWriter = null;
//            fileWriter = new FileWriter(fakeLog4jConfig, true);
//            fileWriter.write(getSLF4JConfigString(newAbsFolder, logFileName));
//            fileWriter.close();
//            System.setProperty("log4j.configurationFile", fakeLog4jConfig.getAbsolutePath());
//            LoggerContext.getContext(false).reconfigure();
//            LoggerContext.getContext(false).updateLoggers();
//            fakeLog4jConfig.delete();
//        } catch (Throwable e)
//        {
////            LOG.fatal(Module.COMMON, e, "Failed to reconfigure log4j.");
//            //
//        }
//    }

    private static String getSLF4JConfigString(List<String> folder, String logFileName)
    {
        String str = Log4j2ConfigBuilder.getInstance().setLogFolders(folder, logFileName).build();
        return str;
    }


    public static boolean haveReceiver(ModuleInterface module, LogLevel logLevel, long type)
    {
        return cellLogger.haveReceiver(module, logLevel, type);
    }

}
