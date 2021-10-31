package com.cell.context.impl;

import com.cell.constants.ContextConstants;
import com.cell.context.IRPCServerCommandContext;
import com.cell.context.RPCServerCommandContext;
import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandProtocolID;
import com.cell.protocol.ContextResponseWrapper;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:40
 */
public abstract class AbstractRPCServerCommandContext extends AbstractBaseContext implements IRPCServerCommandContext
{

    public AbstractRPCServerCommandContext(RPCServerCommandContext commandContext)
    {
        super(commandContext);
    }


    @Override
    public void discard()
    {
        this.response(ContextResponseWrapper.builder()
                .status(ContextConstants.FAIL)
                .ret("BAD REQUEST")
                .msg("BAD REQUEST")
                .build());
    }

    public CommandProtocolID getProtocolID()
    {
        return this.getRPCServerCommandContext().getProtocolID();
    }

    protected RPCServerCommandContext getRPCServerCommandContext()
    {
        return (RPCServerCommandContext) this.getCommandContext();
    }


    @Override
    protected boolean isSetOrExpired()
    {
        return false;
    }
}
