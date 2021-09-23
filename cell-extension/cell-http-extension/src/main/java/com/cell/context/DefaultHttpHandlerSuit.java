package com.cell.context;

import com.cell.channel.IChannel;
import com.cell.command.IHttpCommand;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.protocol.CommandContext;
import com.cell.reactor.IHttpReactor;
import com.cell.services.impl.AbstractHandlerSuit;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 07:17
 */
@Data
public class DefaultHttpHandlerSuit extends AbstractHandlerSuit implements IHttpHandlerSuit
{
    private CommandContext commandContext;
    private IHttpReactor reactor;
    private Class<? extends IHttpCommand> cmd;

    public DefaultHttpHandlerSuit(IChannel<IHandler, IChainHandler> channel, CommandContext commandContext, IHttpReactor reactor,Class<? extends  IHttpCommand>cmd)
    {
        super(channel);
        this.commandContext = commandContext;
        this.reactor = reactor;
        this.cmd=cmd;
    }

    @Override
    public void discard()
    {

    }

    @Override
    public IHttpCommandContext getBuzContext()
    {
        DefaultHttpCommandContext ret = new DefaultHttpCommandContext(this.commandContext);
        ret.setReactor(this.reactor);
        ret.setCommand(cmd);
        return ret;
    }
}
