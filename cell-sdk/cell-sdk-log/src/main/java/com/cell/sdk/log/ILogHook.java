package com.cell.sdk.log;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-25 21:32
 */
//public interface ILogHook extends IHook<LogEntry>
public interface ILogHook
{
    void hook(LogEntry t);
}
