package com.cell.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachable;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-07 22:13
 */
public interface LoggerProxy extends Logger, AppenderAttachable<ILoggingEvent>, Serializable
{
}
