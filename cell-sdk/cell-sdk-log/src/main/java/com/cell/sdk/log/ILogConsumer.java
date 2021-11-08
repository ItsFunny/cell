package com.cell.sdk.log;


import com.cell.sdk.log.consumer.IEventConsumer;

import java.util.List;
import java.util.Optional;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-25 21:09
 */
public interface ILogConsumer extends IEventConsumer<ILogEvent, ILogEventResult>
{
    boolean logAble(LogLevel logLevel);

    // 4. 通过logType+logLevel 获取自定义的hooks
    Optional<List<ILogHook>> grapHooks();
}
