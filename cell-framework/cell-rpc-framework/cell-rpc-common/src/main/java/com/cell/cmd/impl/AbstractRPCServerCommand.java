package com.cell.cmd.impl;

import com.cell.base.core.protocol.*;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.header.DefaultRPCHeader;
import com.cell.base.core.serialize.IInputArchive;
import com.cell.base.core.serialize.JsonInput;
import com.cell.utils.RPCUtils;
import lombok.Data;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:08
 */
@Data
public abstract class AbstractRPCServerCommand extends AbstractCommand implements IRPCServerCommand
{

    public AbstractRPCServerCommand()
    {
        super();
    }

    @Override
    protected IHead newHead()
    {
        return new DefaultRPCHeader(this);
    }

    @Override
    protected void onMakeCouple(ICommand couple)
    {

    }

    @Override
    protected IInputArchive getInputArchiveFromCtx(IBuzzContext c) throws Exception
    {
        IServerRequest request = c.getCommandContext().getRequest();
        return JsonInput.createArchive(RPCUtils.readStringFromRequest(request));
    }

    @Override
    protected void doExecute(IBuzzContext ctx, Object bo) throws IOException
    {
        this.onExecute((IRPCServerCommandContext) ctx, bo);
    }

    protected abstract void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException;
}

