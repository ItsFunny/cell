package com.cell.protocol;


import com.cell.concurrent.base.EventExecutor;
import com.cell.services.IHandlerSuit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 14:31
 */
public interface ICommandSuit extends IHandlerSuit
{
    IBuzzContext getBuzContext();

    void setCommandEventExecutor(EventExecutor eventExecutor);

    EventExecutor getCommandEventExecutor();
}
