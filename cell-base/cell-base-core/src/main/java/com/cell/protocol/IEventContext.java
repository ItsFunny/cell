package com.cell.protocol;

import com.cell.events.IEvent;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-18 22:30
 */
public interface IEventContext extends IContext
{
    IEvent getEvent();

    @Override
    default void discard() { }
}
