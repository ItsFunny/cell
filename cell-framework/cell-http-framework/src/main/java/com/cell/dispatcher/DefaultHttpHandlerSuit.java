package com.cell.dispatcher;

import com.cell.command.IHttpCommand;
import com.cell.context.DefaultHttpCommandContext;
import com.cell.context.IHttpHandlerSuit;
import com.cell.protocol.CommandContext;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.impl.AbstractCommandSuit;
import com.cell.reactor.IHttpReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 01:00
 */
public class DefaultHttpHandlerSuit extends AbstractCommandSuit implements IHttpHandlerSuit
{
    private IHttpReactor reactor;
    private Class<? extends IHttpCommand> cmd;

    public DefaultHttpHandlerSuit(CommandContext commandContext, IHttpReactor reactor, Class<? extends IHttpCommand> cmd)
    {
        super(commandContext);
        this.reactor = reactor;
        this.cmd = cmd;
    }

//    public DefaultHttpHandlerSuit(IChannel<IHandler, IChainHandler> channel, CommandContext commandContext, IHttpReactor reactor, Class<? extends IHttpCommand> cmd)
//    {
//        this.commandContext = commandContext;
//        this.reactor = reactor;
//        this.cmd = cmd;
//    }

    @Override
    public void discard()
    {

    }

    @Override
    public IBuzzContext getBuzContext()
    {
        DefaultHttpCommandContext ret = new DefaultHttpCommandContext(this.getCommandContext());
        ret.setReactor(this.reactor);
        ret.setCommand(cmd);
        ret.setEventExecutor(this.getCommandEventExecutor());
        return ret;
    }
}
