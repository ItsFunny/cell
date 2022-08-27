package com.cell.sdk.log.impl;

import com.cell.sdk.log.LogEntry;

public interface ILogFilter
{
    default boolean logAble(LogEntry logEntry)
    {
        return true;
    }
}
