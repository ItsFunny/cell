package com.cell.log.impl;

import com.cell.base.common.models.Module;
import com.cell.sdk.log.LOG;
import com.cell.sdk.log.LogTypeEnums;
import com.cell.sdk.log.impl.DefaultCellLogger;
import com.cell.sdk.log.impl.DefaultConsoleLogBackConsumer;
import com.cell.sdk.log.impl.DefaultFileLogBackEventConsumer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCellLoggerTest
{

    @Test
    public void registerConsumers()
    {
        DefaultConsoleLogBackConsumer consoleLogBackConsumer = new DefaultConsoleLogBackConsumer();
        consoleLogBackConsumer.initOnce(null);
        DefaultCellLogger cellLogger = new DefaultCellLogger();
        cellLogger.registerConsumers(consoleLogBackConsumer);
        cellLogger.info(LogTypeEnums.NORMAL, null, "addd", null);
    }

    @Test
    public void test111()
    {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("123");
    }

    @Test
    public void testFileLog()
    {
        DefaultFileLogBackEventConsumer fileLogBackEventConsumer = new DefaultFileLogBackEventConsumer();
        fileLogBackEventConsumer.initOnce(null);
        DefaultCellLogger cellLogger = new DefaultCellLogger();
        cellLogger.info(LogTypeEnums.NORMAL, null, "add");
        cellLogger.warn(LogTypeEnums.NORMAL, null, "add");
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testSlf4jLog()
    {
        logger.info("123");
        logger.info("444:{}", 1);
    }

    @Test
    public void testLog()
    {
        LOG.debug("debug");
        LOG.info("info");
        LOG.warn("warn");
        LOG.info("123333,{}", 1);
        LOG.error("error");
    }

    @Test
    public void testM()
    {
        LOG.minfo(Module.FABRIC_CLIENT, "123 {}", 4);
    }
}