package com.cell.base.core.consumers;

import com.cell.base.core.events.IEvent;
import com.cell.base.core.events.IEventResult;

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
