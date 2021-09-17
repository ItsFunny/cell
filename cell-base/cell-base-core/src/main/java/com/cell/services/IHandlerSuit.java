package com.cell.services;


import com.cell.channel.IChannel;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.IContext;

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
