package org.slf4j.impl;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import com.cell.log.bridge.LOGLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder
{
    private static final StaticLoggerBinder BINDER = new StaticLoggerBinder();
    private LOGLoggerFactory LOG_LOGGER_FACTORY = new LOGLoggerFactory();

    @Override
    public ILoggerFactory getLoggerFactory()
    {
        return LOG_LOGGER_FACTORY;
    }

    @Override
    public String getLoggerFactoryClassStr()
    {
        return LOG_LOGGER_FACTORY.getClass().getName();
    }

    public static final LOGLoggerFactory getLogLoggerFactory()
    {
        return BINDER.LOG_LOGGER_FACTORY;
    }

    public static final StaticLoggerBinder getSingleton()
    {
        return BINDER;
    }

    static
    {
        BINDER.init();
    }

    void init()
    {
        try
        {
            try
            {
                this.LOG_LOGGER_FACTORY.initOnce(null);
            } catch (Exception var2)
            {
                Util.report("Failed to auto configure default logger context", var2);
            }
        } catch (Exception var3)
        {
            Util.report("Failed to instantiate [" + LoggerContext.class.getName() + "]", var3);
        }

    }
}
