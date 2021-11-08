package com.cell.base.core.protocol;


import com.cell.base.core.services.IHandlerSuit;
import com.cell.base.core.concurrent.base.EventExecutor;

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
