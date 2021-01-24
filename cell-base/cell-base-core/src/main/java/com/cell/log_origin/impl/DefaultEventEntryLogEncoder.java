package com.cell.log_origin.impl;

import com.cell.log_origin.ILogEncoder;
import com.cell.log_origin.LogEntry;
import com.cell.log_origin.LogEvent;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:37
 */
public class DefaultEventEntryLogEncoder implements ILogEncoder<LogEvent, LogEntry>
{
    // FIXME ,从缓存中取得此次的sequenceId,如果没有则设置,
    @Override
    public LogEntry convert(LogEvent logEvent)
    {
        return null;
    }
}
