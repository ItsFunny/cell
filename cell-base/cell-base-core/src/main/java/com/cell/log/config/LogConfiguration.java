package com.cell.log.config;

import com.cell.log.LOG;
import com.cell.log.LogLevel;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-10 20:54
 */
@Data
public class LogConfiguration
{
    private static final LogConfiguration LOG_CONFIGURATION = new LogConfiguration();
    private LogLevel logLevel = LogLevel.DEBUG;
    private boolean enableFile;

    public static void setLogLevel(LogLevel logLevel)
    {
        LOG_CONFIGURATION.logLevel = logLevel;
    }

    // FIXME,需要提供once
    public static void setEnableFile(boolean enable)
    {
        LOG_CONFIGURATION.enableFile = enable;
    }

    public static LogConfiguration getInstance()
    {
        return LOG_CONFIGURATION;
    }
}
