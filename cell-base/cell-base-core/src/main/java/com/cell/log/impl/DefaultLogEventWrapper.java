package com.cell.log.impl;

import com.cell.log.ILogEvent;
import com.cell.log.LogEntry;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-25 21:45
 */
@Data
public class DefaultLogEventWrapper implements ILogEvent
{

    private LogEntry logEntry;

    @Override
    public LogEntry getLogEntry()
    {
        return this.logEntry;
    }

    public DefaultLogEventWrapper(LogEntry logEntry)
    {
        this.logEntry = logEntry;
    }
}
