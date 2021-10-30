package com.cell.log.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.OutputStreamAppender;
import com.cell.context.InitCTX;
import com.cell.exceptions.ConfigException;
import com.cell.log.factory.DefaultSlf4jLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-02-01 21:56
 */
public class DefaultConsoleLogBackConsumer extends AbstractLogBackLogEventConsumer
{
    @Override
    protected void onInit(InitCTX ctx)
    {
        this.logger=DefaultSlf4jLoggerFactory.getInstance().getConsoleLogger();
    }
}
