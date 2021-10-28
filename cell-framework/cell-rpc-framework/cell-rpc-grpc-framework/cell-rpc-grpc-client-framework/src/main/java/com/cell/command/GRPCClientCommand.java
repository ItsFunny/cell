package com.cell.command;

import com.cell.protocol.AbstractCommand;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.ICommand;
import com.cell.protocol.IHead;
import com.cell.serialize.IInputArchive;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 14:42
 */
public class GRPCClientCommand extends AbstractCommand implements IGRPCClientCommand
{


    @Override
    protected IHead newHead()
    {
        return null;
    }

    @Override
    protected void onMakeCouple(ICommand couple)
    {

    }

    @Override
    protected IInputArchive getInputArchiveFromCtx(IBuzzContext c) throws Exception
    {
        return null;
    }

    @Override
    protected void doExecute(IBuzzContext ctx, Object bo) throws IOException
    {

    }
}
