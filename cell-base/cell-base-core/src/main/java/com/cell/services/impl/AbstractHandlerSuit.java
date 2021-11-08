package com.cell.services.impl;

import com.cell.channel.IChannel;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.CommandContext;
import com.cell.services.IHandlerSuit;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 05:10
 */
@Data
public abstract class AbstractHandlerSuit implements IHandlerSuit
{
    private CommandContext commandContext;

    public AbstractHandlerSuit(CommandContext commandContext)
    {
        this.commandContext = commandContext;
    }


    @Override
    public IChannel<IHandler, IChainHandler> channel()
    {
        return this.commandContext.getChannel();
    }
//
//    @Override
//    public Pipeline pipeline()
//    {
//        return this.pipeline;
//    }
//
//    @Override
//    public IHandlerContext fireHandlerRegistered()
//    {
//        return this;
//    }
//
//    @Override
//    public IHandlerContext fireChannelRead(Object msg)
//    {
//        return this;
//    }
}
