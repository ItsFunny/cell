package com.cell.suit;

import com.cell.base.core.protocol.impl.AbstractCommandSuit;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.context.RPCServerCommandContext;
import com.cell.context.impl.DefaultRPCServerCommandContext;
import com.cell.reactor.IRPCServerReactor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 18:29
 */
@Data
public class DefaultServerRPCCommandSuit extends AbstractCommandSuit implements IRPCHandlerSuit
{
    private IRPCServerReactor reactor;
    private Class<? extends IRPCServerCommand> command;

    public DefaultServerRPCCommandSuit(RPCServerCommandContext ctx, IRPCServerReactor serverReactor, Class<? extends IRPCServerCommand> serverCommand)
    {
        super(ctx);
        this.reactor = serverReactor;
        this.command = serverCommand;
    }

    @Override
    public void discard()
    {

    }

    @Override
    public IRPCServerCommandContext getBuzContext()
    {
        DefaultRPCServerCommandContext ret = new DefaultRPCServerCommandContext((RPCServerCommandContext) this.getCommandContext());
        return ret;
    }
}
