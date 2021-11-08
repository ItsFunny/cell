package com.cell.base.core.services;


import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.channel.IChannel;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-18 19:31
 */
public interface IHandlerSuit extends IContext
{
    IChannel<IHandler, IChainHandler> channel();
}
