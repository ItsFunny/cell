package com.cell.sdk.log.bridge;

import com.cell.base.common.context.InitCTX;
import com.cell.base.common.models.Module;
import com.cell.base.common.context.AbstractInitOnce;
import com.cell.sdk.log.LOG;
import com.cell.sdk.log.LogLevel;
import com.cell.sdk.log.LogTypeEnums;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;


public class LOGLoggerWrapper extends AbstractInitOnce implements Logger, InternalLogger
{

    private Module module;

    public LOGLoggerWrapper(Module module)
    {
        this.module = module;
    }

    @Override
    public String getName()
    {
        return "LOG";
    }

    @Override
    public String name()
    {
        return module.name();
    }

    @Override
    public boolean isTraceEnabled()
    {
        return LOG.haveReceiver(module, LogLevel.TRACE, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public boolean isTraceEnabled(Marker marker)
    {
        return LOG.haveReceiver(module, LogLevel.TRACE, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public void trace(String msg)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void trace(String format, Object arg)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void trace(String format, Object arg1, Object arg2)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void trace(String format, Object... argArray)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void trace(String msg, Throwable t)
    {
        LOG.trace(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void trace(Throwable throwable)
    {
        this.trace("", throwable);
    }

    @Override
    public void trace(Marker marker, String msg)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray)
    {
        LOG.trace(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t)
    {
        LOG.trace(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public boolean isDebugEnabled()
    {
        return LOG.haveReceiver(module, LogLevel.DEBUG, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public boolean isDebugEnabled(Marker marker)
    {
        return LOG.haveReceiver(module, LogLevel.DEBUG, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public void debug(String msg)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void debug(String format, Object arg)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void debug(String format, Object... argArray)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void debug(String msg, Throwable t)
    {
        LOG.debug(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void debug(Throwable throwable)
    {
        this.debug(null, throwable);
    }

    @Override
    public void debug(Marker marker, String msg)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void debug(Marker marker, String format, Object... argArray)
    {
        LOG.debug(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t)
    {
        LOG.debug(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public boolean isInfoEnabled()
    {
        return LOG.haveReceiver(module, LogLevel.INFO, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public boolean isInfoEnabled(Marker marker)
    {
        return LOG.haveReceiver(module, LogLevel.INFO, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public void info(String msg)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void info(String format, Object arg)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void info(String format, Object arg1, Object arg2)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void info(String format, Object... argArray)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void info(String msg, Throwable t)
    {
        LOG.info(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void info(Throwable throwable)
    {
        this.info(null, throwable);

    }

    @Override
    public void info(Marker marker, String msg)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void info(Marker marker, String format, Object... argArray)
    {
        LOG.info(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void info(Marker marker, String msg, Throwable t)
    {
        LOG.info(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public boolean isWarnEnabled()
    {
        return LOG.haveReceiver(module, LogLevel.WARN, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public boolean isWarnEnabled(Marker marker)
    {
        return LOG.haveReceiver(module, LogLevel.WARN, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public void warn(String msg)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void warn(String format, Object arg)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void warn(String format, Object... argArray)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void warn(String msg, Throwable t)
    {
        LOG.warn(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void warn(Throwable throwable)
    {
        this.warn(null, throwable);
    }

    @Override
    public void warn(Marker marker, String msg)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void warn(Marker marker, String format, Object... argArray)
    {
        LOG.warn(module, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t)
    {
        LOG.warn(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public boolean isErrorEnabled()
    {
        return LOG.haveReceiver(module, LogLevel.ERROR, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public boolean isErrorEnabled(Marker marker)
    {
        return LOG.haveReceiver(module, LogLevel.ERROR, LogTypeEnums.THIRD_PARTY.getValue());
    }

    @Override
    public void error(String msg)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void error(String format, Object arg)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void error(String format, Object arg1, Object arg2)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void error(String format, Object... argArray)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void error(String msg, Throwable t)
    {
        LOG.error(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void error(Throwable throwable)
    {
        this.error(null, throwable);

    }

    @Override
    public boolean isEnabled(InternalLogLevel internalLogLevel)
    {

        return LOG.haveReceiver(module, internalLoglevlTOLevel(internalLogLevel), LogTypeEnums.THIRD_PARTY.getCode());
    }

    private LogLevel internalLoglevlTOLevel(InternalLogLevel logLevel)
    {
        switch (logLevel)
        {
            case TRACE:
                return LogLevel.TRACE;
            case DEBUG:
                return LogLevel.DEBUG;
            case INFO:
                return LogLevel.INFO;
            case WARN:
                return LogLevel.WARN;
            case ERROR:
                return LogLevel.ERROR;
        }
        return LogLevel.INFO;
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String s)
    {
        switch (internalLogLevel)
        {
            case TRACE:
                this.trace(s);
                return;
            case DEBUG:
                this.debug(s);
                return;
            case INFO:
                this.info(s);
                return;
            case ERROR:
                this.error(s);
                return;
            case WARN:
                this.warn(s);
                return;
        }
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String s, Object o)
    {
        switch (internalLogLevel)
        {
            case TRACE:
                this.trace(s, o);
                return;
            case DEBUG:
                this.debug(s, o);
                return;
            case INFO:
                this.info(s, o);
                return;
            case ERROR:
                this.error(s, o);
                return;
            case WARN:
                this.warn(s, o);
                return;
        }
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String s, Object o, Object o1)
    {
        switch (internalLogLevel)
        {
            case TRACE:
                this.trace(s, o, o1);
                return;
            case DEBUG:
                this.debug(s, o, o1);
                return;
            case INFO:
                this.info(s, o, o1);
                return;
            case ERROR:
                this.error(s, o, o1);
                return;
            case WARN:
                this.warn(s, o, o1);
                return;
        }
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String s, Object... objects)
    {
        switch (internalLogLevel)
        {
            case TRACE:
                this.trace(s, objects);
                return;
            case DEBUG:
                this.debug(s, objects);
                return;
            case INFO:
                this.info(s, objects);
                return;
            case ERROR:
                this.error(s, objects);
                return;
            case WARN:
                this.warn(s, objects);
                return;
        }
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String s, Throwable throwable)
    {
        switch (internalLogLevel)
        {
            case TRACE:
                this.trace(s, throwable);
                return;
            case DEBUG:
                this.debug(s, throwable);
                return;
            case INFO:
                this.info(s, throwable);
                return;
            case ERROR:
                this.error(s, throwable);
                return;
            case WARN:
                this.warn(s, throwable);
                return;
        }
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, Throwable throwable)
    {
        switch (internalLogLevel)
        {
            case TRACE:
                this.trace(throwable);
                return;
            case DEBUG:
                this.debug(throwable);
                return;
            case INFO:
                this.info(throwable);
                return;
            case ERROR:
                this.error(throwable);
                return;
            case WARN:
                this.warn(throwable);
                return;
        }
    }

    @Override
    public void error(Marker marker, String msg)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void error(Marker marker, String format, Object... argArray)
    {
        LOG.error(module, null, LogTypeEnums.THIRD_PARTY, MessageFormatter.arrayFormat(format, argArray).getMessage());
    }

    @Override
    public void error(Marker marker, String msg, Throwable t)
    {
        LOG.error(module, t, LogTypeEnums.THIRD_PARTY, msg);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        LOG.refresh();
    }
}
