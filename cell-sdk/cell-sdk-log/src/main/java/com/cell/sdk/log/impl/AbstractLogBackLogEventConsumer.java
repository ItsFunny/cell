package com.cell.sdk.log.impl;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.sdk.log.*;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * @author Charlie
 * @When
 * @Description 默认的控制台输出对象
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-25 21:10
 */
public abstract class AbstractLogBackLogEventConsumer extends AbstractInitOnce implements ILogConsumer
{

    protected Logger logger;
    protected volatile List<ILogHook> hooks;
    protected IEntryLayOut layOut;


    @Override
    public ILogEventResult consume(ILogEvent logEvent)
    {
        LogEntry entry = logEvent.getLogEntry();
        if (!CollectionUtils.isEmpty(hooks))
        {
            hooks.forEach(h ->
                    h.hook(entry));
        }

        this.logLevelToSlf4jLogLevel(this.logger, entry);
        return null;
    }

    protected void logLevelToSlf4jLogLevel(Logger logger, LogEntry entry)
    {
        if (null == logger)
        {
            return;
        }
        LogLevel logLevel = entry.getLogLevel();
        String msg = this.layOut != null ? this.layOut.layOut(entry) : entry.getMessage();
        Object[] objects = entry.getObjects();
        logLevelToSlf4jLogLevel(logger, logLevel, msg, objects);
    }

    protected void logLevelToSlf4jLogLevel(Logger logger, LogLevel logLevel, String msg, Object[] objects)
    {
        switch (logLevel)
        {
            case DEBUG:
                logger.debug(msg, objects);
                break;
            case INFO:
                logger.info(msg, objects);
                break;
            case WARN:
                logger.warn(msg, objects);
                break;
            case ERROR:
                logger.error(msg, objects);
                break;
        }
    }

    @Override
    public boolean logAble(LogLevel logLevel)
    {
        return true;
    }

    @Override
    public Optional<List<ILogHook>> grapHooks()
    {
        return Optional.ofNullable(this.hooks);
    }
}
