package com.cell.base.core.services.impl;

import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.protocol.CommandContext;
import com.cell.base.core.services.IHandlerSuit;
import com.cell.base.core.channel.IChannel;
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
