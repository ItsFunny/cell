package com.cell.cmd.impl;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.header.DefaultRPCHeader;
import com.cell.protocol.*;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.JsonInput;
import com.cell.utils.ClassUtil;
import com.cell.utils.RPCUtils;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:08
 */
public abstract class AbstractRPCServerCommand extends AbstractCommand implements IRPCServerCommand
{
    private RPCServerCmdAnno cmd;

    public AbstractRPCServerCommand()
    {
        super();
        this.cmd = (RPCServerCmdAnno) ClassUtil.getAnnotation(this.getClass(), RPCServerCmdAnno.class);
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

