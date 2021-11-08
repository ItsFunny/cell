package com.cell.sdk.log;

import com.cell.base.common.events.IEvent;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-25 21:11
 */
public interface ILogEvent extends IEvent
{
    LogEntry getLogEntry();
}
