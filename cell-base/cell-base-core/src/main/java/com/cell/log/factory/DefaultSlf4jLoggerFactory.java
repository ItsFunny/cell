package com.cell.log.factory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;
import com.cell.grpc.common.config.AbstractInitOnce;
import com.cell.grpc.common.config.IInitOnce;
import com.cell.context.InitCTX;
import com.cell.exceptions.ValidateException;
import com.cell.log.LogLevel;
import com.cell.log.Util;
import com.cell.log.bridge.ClassBridge;
import com.cell.log.bridge.DefaultCellLoggerContext;
import com.cell.log.bridge.LOGLoggerWrapper;
import com.cell.models.Module;
import com.cell.utils.StringUtils;
import com.cell.validators.IValidator;
import lombok.Data;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.impl.StaticLoggerBinder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-31 23:27
 */
@Data
public class DefaultSlf4jLoggerFactory implements ILoggerFactory, IInitOnce
{
    private static final String console_logger = "cell_console_logger";

    @Override
    public void initOnce(InitCTX ctx)
    {
        this.new InternalInitClass().initOnce(ctx);
    }

    class InternalInitClass extends AbstractInitOnce
    {
        @Override
        protected void onInit(InitCTX ctx)
        {
            try
            {
                (new ContextInitializer(DefaultSlf4jLoggerFactory.this.defaultLoggerContext)).autoConfig();
                if (!StatusUtil.contextHasStatusListener(DefaultSlf4jLoggerFactory.this.defaultLoggerContext))
                {
                    StatusPrinter.printInCaseOfErrorsOrWarnings(DefaultSlf4jLoggerFactory.this.defaultLoggerContext);
                }
//                DefaultSlf4jLoggerFactory.this.contextSelectorBinder.init(DefaultSlf4jLoggerFactory.this.defaultLoggerContext, DefaultSlf4jLoggerFactory.this.KEY);
                loadService();
                getConsoleLogger();
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static final DefaultSlf4jLoggerFactory INSTANCE = new DefaultSlf4jLoggerFactory();
    private static final LOGLoggerWrapper COMMON_LOGGER_WRAPPER = new LOGLoggerWrapper(Module.THIRD_PARTY);
    private static Map<String, LOGLoggerWrapper> logWrapperMap = new HashMap<>();
    private DefaultCellLoggerContext defaultLoggerContext = new DefaultCellLoggerContext();

    private final ContextSelectorStaticBinder contextSelectorBinder = ContextSelectorStaticBinder.getSingleton();
    private Object KEY = new Object();


    static
    {
        swapSl4j();
        for (Map.Entry<String, Module> entry : ClassBridge.moduleMap.entrySet())
        {
            logWrapperMap.put(entry.getKey(), new LOGLoggerWrapper(entry.getValue()));
        }
        INSTANCE.initOnce(null);
    }

    static void swapSl4j()
    {
        try
        {
            // 两种方法结合干掉 原先的 slf4j
            // 1. 因为类加载是并发进行的,期间肯定会有getLogger 调用 ,所以当我们执行到这一步的时候,首先将之前生成的logger全部重新修饰
            // 2. 替换掉contextSelector,使得之后的所有logger都走我们自己的context
            StaticLoggerBinder staticLoggerBinder = StaticLoggerBinder.getSingleton();
            Field defaultLoggerContext = staticLoggerBinder.getClass().getDeclaredField("defaultLoggerContext");
            defaultLoggerContext.setAccessible(true);
            LoggerContext context = (LoggerContext) defaultLoggerContext.get(staticLoggerBinder);
//            // FIXME ,还是有大问题
            synchronized (staticLoggerBinder)
            {
                List<ch.qos.logback.classic.Logger> loggerList = context.getLoggerList();
                for (ch.qos.logback.classic.Logger logger : loggerList)
                {
                    getInstance().getCellLogger(logger.getName());
                }
                defaultLoggerContext.set(staticLoggerBinder, getInstance().getDefaultLoggerContext());
                ContextSelectorStaticBinder singleton = ContextSelectorStaticBinder.getSingleton();
                Field field = singleton.getClass().getDeclaredField("contextSelector");
                field.setAccessible(true);
                field.set(singleton, new DefaultCellLoggerContextSelector(getInstance().defaultLoggerContext));
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public Logger getLogger(String name)
    {
        for (Map.Entry<String, LOGLoggerWrapper> entry : logWrapperMap.entrySet())
        {
            if (name.startsWith(entry.getKey()))
            {
                LOGLoggerWrapper value = entry.getValue();
                value.initOnce(null);
                return value;
            }
        }
//        COMMON_LOGGER_WRAPPER.initOnce(null);
//        return this.getCellLogger(name);
        return COMMON_LOGGER_WRAPPER;
    }

    public Logger getFileCellLogger(String name)
    {
        ch.qos.logback.classic.Logger logger = this.defaultLoggerContext.getLogger(name);
        return logger;
    }

    public Logger getCellLogger(String name)
    {
        ch.qos.logback.classic.Logger logger = this.defaultLoggerContext.getLogger(name);
        if (this.defaultLoggerContext.needDecorate(name) && !name.equalsIgnoreCase("root"))
        {
            this.setupByName(name, logger);
            this.defaultLoggerContext.tag(name);
        }
        return logger;
    }

    //    private static CellLogFactoryContext context = new CellLogFactoryContext((LoggerContext) LoggerFactory.getILoggerFactory());
//    private static LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//    private static LOGLoggerFactory context = (LOGLoggerFactory) LoggerFactory.getILoggerFactory();


    private static final String ENV_SERVICE_NAME = "ENV_SERVICE_NAME";
    private static String serviceName = null;


    private void loadService()
    {
        serviceName = System.getenv(ENV_SERVICE_NAME);
    }

    @Data
    public static class AppenderProperty implements IValidator
    {
        protected boolean fileAppender;
        protected Long type;
        protected Level level;
        protected String appenderName;
        //        protected String pattern = "%d %p (%file:%line\\)- %m%n";
        protected String pattern = "%m%n";
        protected LogAppenderDecorator decorator;

        @Override
        public void valid() throws ValidateException
        {
            this.level = this.level == null ? Level.INFO : this.level;
            this.appenderName = DefaultSlf4jLoggerFactory.getAppenderName(Util.getAbsLogFolder(null), this.type);
            if (StringUtils.isEmpty(this.pattern)) throw new ValidateException("pattern不可为空");
        }
    }

    public interface LogAppenderDecorator
    {
        void decorate(OutputStreamAppender appender, LoggerContext context);
    }

    @Data
    public static class FileAppenderProperty extends AppenderProperty
    {
        private String fileName;
        private String maxFileSize;
        private Integer maxHistory;
        private String totalSizeGap;
        private String logFolder;

        @Override
        public void valid() throws ValidateException
        {
            super.valid();
            if (StringUtils.isEmpty(fileName)) throw new ValidateException("fileName不可为空");
            this.logFolder = Util.getAbsLogFolder(logFolder);
            this.maxFileSize = StringUtils.isEmpty(this.maxFileSize) ? "128MB" : this.maxFileSize;
            this.maxHistory = StringUtils.isEmpty(this.maxHistory) ? 15 : this.maxHistory;
            this.totalSizeGap = StringUtils.isEmpty(this.totalSizeGap) ? "32GB" : this.totalSizeGap;
        }
    }

    @Data
    public static class ConsoleAppenderProperty extends AppenderProperty
    {

    }

    private void setupByName(String name, ch.qos.logback.classic.Logger logger)
    {
        DefaultSlf4jLoggerFactory.ConsoleAppenderProperty consoleAppenderProperty = new DefaultSlf4jLoggerFactory.ConsoleAppenderProperty();
        consoleAppenderProperty.setAppenderName(name);
        consoleAppenderProperty.setFileAppender(false);
        consoleAppenderProperty.setLevel(Level.DEBUG);
        OutputStreamAppender debugAppender = this.getAppender(consoleAppenderProperty);
        consoleAppenderProperty.setLevel(Level.INFO);
        OutputStreamAppender infoAppender = this.getAppender(consoleAppenderProperty);
        consoleAppenderProperty.setLevel(Level.WARN);
        OutputStreamAppender warnAppender = this.getAppender(consoleAppenderProperty);
        consoleAppenderProperty.setLevel(Level.ERROR);
        OutputStreamAppender errorAppender = this.getAppender(consoleAppenderProperty);

        logger.setAdditive(false);
        logger.addAppender(debugAppender);
        logger.addAppender(infoAppender);
        logger.addAppender(warnAppender);
        logger.addAppender(errorAppender);
    }

    /**
     * 通过传入的名字和级别，动态设置appender
     *
     * @return
     */
    public OutputStreamAppender getAppender(AppenderProperty property)
    {
        //这里是可以用来设置appender的，在xml配置文件里面，是这种形式：
        OutputStreamAppender appender = property.isFileAppender() ? new RollingFileAppender() : new ConsoleAppender();
        if (property.getDecorator() == null)
        {
            defaultDecorateAppender(appender, property, this.defaultLoggerContext);
        } else
        {
            property.getDecorator().decorate(appender, this.defaultLoggerContext);
        }
        appender.start();
        return appender;
    }

    private OutputStreamAppender defaultDecorateAppender(OutputStreamAppender appender, AppenderProperty property, LoggerContext context)
    {
        property.valid();
        appender.setContext(context);

        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        //appender的name属性
        appender.setName(property.appenderName);
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        encoder.setContext(context);
        //设置格式
        encoder.setPattern(property.getPattern());
        encoder.start();
        appender.setEncoder(encoder);

        if (appender instanceof RollingFileAppender)
        {
            appender = decorateDefaultFileAppender((RollingFileAppender) appender, (FileAppenderProperty) property, context);
        } else if (appender instanceof ConsoleAppender)
        {
            appender = decorateDefaultConsoleAppender((ConsoleAppender) appender, (ConsoleAppenderProperty) property, context);
        }
        return appender;
    }

    private static OutputStreamAppender decorateDefaultConsoleAppender(ConsoleAppender appender, ConsoleAppenderProperty property, LoggerContext context)
    {
        //这里设置级别过滤器
        LevelController levelController = new LevelController();
//        ThresholdFilter filter = levelController.getThresholdFilters(property.getLevel());
        LevelFilter filter = levelController.getLevelFilter(property.getLevel());
        appender.addFilter(filter);
        appender.setContext(context);
        return appender;
    }

    private static OutputStreamAppender decorateDefaultFileAppender(RollingFileAppender appender, FileAppenderProperty property, LoggerContext context)
    {
        //这里设置级别过滤器
        LevelController levelController = new LevelController();
        ThresholdFilter filter = levelController.getThresholdFilters(property.getLevel());
        appender.addFilter(filter);

        String logFolder = property.logFolder;
        String folder = property.type == null ? logFolder : logFolder + "/" + property.type.toString().toLowerCase();
        String fileName = StringUtils.isEmpty(serviceName) ? folder + "/" + property.fileName + ".log" : folder + "/" + serviceName + "-" + property.fileName + ".log";

        appender.setFile(fileName);
        appender.setAppend(true);
        appender.setPrudent(false);


        //设置文件创建时间及大小的类
        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
        //设置父节点是appender
        policy.setParent(appender);
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        policy.setContext(context);
        //最大日志文件大小
        policy.setMaxFileSize(FileSize.valueOf(property.getMaxFileSize()));
        //设置文件名模式
        String filePattern = folder + "/%d{yyyy-MM-dd}/" + property.fileName + "-all-%i.log";
        policy.setFileNamePattern(filePattern);
        //设置最大历史记录为15条
        policy.setMaxHistory(property.getMaxHistory());
        //总大小限制
        policy.setTotalSizeCap(FileSize.valueOf(property.getTotalSizeGap()));

        policy.start();

        appender.setRollingPolicy(policy);
        return appender;
    }

    static String getAppenderName(String baseFolder, Long type)
    {
        return baseFolder.hashCode() + "_" + getLoggerName(type);
    }

//    static String getAppenderName(String baseFolder, Long type, String addType)
//    {
//        return baseFolder.hashCode() + "_" + getLoggerName(type, addType);
//    }

    static String getAppenderName(String baseFolder, Long type, LogLevel level)
    {
        return baseFolder.hashCode() + "_" + getLoggerName(type, level);
    }

    static String getLoggerName(Long type, LogLevel level)
    {
        return (type == null ? "DEFAULT" : type.toString()) + "_LOG_LEVEL_" + level.toString();
    }

    static String getLoggerName(Long type)
    {
        return (type == null ? "DEFAULT" : type.toString()) + "_ALL_LOGS";
    }

    public static class LevelController
    {
        /**
         * 通过level设置过滤器
         *
         * @param level
         * @return
         */
        public LevelFilter getLevelFilter(Level level)
        {
            LevelFilter levelFilter = new LevelFilter();
            levelFilter.setLevel(level);
            levelFilter.setOnMatch(FilterReply.ACCEPT);
            levelFilter.setOnMismatch(FilterReply.DENY);
            levelFilter.start();
            return levelFilter;
        }

        public ThresholdFilter getThresholdFilters(Level level)
        {
            ThresholdFilter filter = new ThresholdFilter();
            filter.setLevel(level.levelStr);
            filter.start();
            return filter;
        }

    }

    public Logger getConsoleLogger()
    {
        return this.getCellLogger(console_logger);
    }

    public static DefaultSlf4jLoggerFactory getInstance()
    {
        return INSTANCE;
    }

}
