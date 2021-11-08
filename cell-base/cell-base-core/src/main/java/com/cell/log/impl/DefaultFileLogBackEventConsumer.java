package com.cell.log.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.OutputStreamAppender;
import com.cell.context.InitCTX;
import com.cell.log.LogLevel;
import com.cell.log.LogTypeEnums;
import com.cell.log.Util;
import com.cell.log.config.LogConfiguration;
import com.cell.log.factory.DefaultSlf4jLoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-02-01 22:32
 */
public class DefaultFileLogBackEventConsumer extends AbstractLogBackLogEventConsumer
{
    private static final String FILE_LOGGER = "file_logger";
    volatile Logger errLogger = null;
    volatile Logger warnLogger = null;
    volatile Logger illegalUserLogger = null;
    volatile Logger gcLogger = null;

    // 额外的配置
    @Override
    protected void onInit(InitCTX ctx)
    {
        if (!LogConfiguration.getInstance().isEnableFile())
        {
            return;
        }

        LogLevel logLevel = LogConfiguration.getInstance().getLogLevel();
        this.logger = setupFile(FILE_LOGGER, null, "all", Arrays.asList(Util.logLevel2LogBackLevel(logLevel)));
        this.errLogger = setupFile("error_logger", null, "error", Arrays.asList(Level.ERROR));
        this.warnLogger = setupFile("warn_logger", null, "warn", Arrays.asList(Level.WARN));
        this.illegalUserLogger = setupFile("illegal_user", null, "illegal_user", Arrays.asList(Level.DEBUG));
        this.gcLogger = setupFile("gc", null, "gc", Arrays.asList(Level.INFO));
        this.setupHooks();
    }

    private void setupHooks()
    {
        this.hooks = new ArrayList<>();
        this.hooks.add((entry) ->
        {
            LogLevel logLevel = entry.getLogLevel();
            Long logType = entry.getLogType();
            String message = entry.getMessage();
            Object[] objects = entry.getObjects();
            if (logLevel.isBiggerOrEqual(LogLevel.ERROR)) this.errLogger.error(message, objects);
            if (logLevel.isBiggerOrEqual(LogLevel.WARN))
            {
                logLevelToSlf4jLogLevel(this.warnLogger, logLevel, message, objects);
            }
            if (logType == LogTypeEnums.GC.getCode())
            {
                logLevelToSlf4jLogLevel(this.gcLogger, logLevel, message, objects);
            }
            if (logType == LogTypeEnums.ILLEDGAL_USER.getCode())
            {
                logLevelToSlf4jLogLevel(this.illegalUserLogger, logLevel, message, objects);
            }

        });

    }

    private Logger setupFile(String name, Long type, String fileName, List<Level> levels)
    {
        DefaultSlf4jLoggerFactory.FileAppenderProperty property = new DefaultSlf4jLoggerFactory.FileAppenderProperty();
        property.setFileAppender(true);
        property.setType(type);
        property.setFileName(fileName);
        List<OutputStreamAppender> appenders = new ArrayList<>();
        for (Level level : levels)
        {
            property.setLevel(level);
            OutputStreamAppender appender = DefaultSlf4jLoggerFactory.getInstance().getAppender(property);
            appenders.add(appender);
        }
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) DefaultSlf4jLoggerFactory.getInstance().getFileCellLogger(name);
        logger.setAdditive(false);
        for (OutputStreamAppender appender : appenders)
        {
            logger.addAppender(appender);
        }
        return logger;
    }
}
