package com.cell.sdk.log.consumer;

import com.cell.base.common.consumers.IConsumer;
import com.cell.base.common.events.IEvent;
import com.cell.base.common.events.IEventResult;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-13 22:57
 */
public interface IEventConsumer<T extends IEvent, V extends IEventResult> extends IConsumer<T, V>
{

}
