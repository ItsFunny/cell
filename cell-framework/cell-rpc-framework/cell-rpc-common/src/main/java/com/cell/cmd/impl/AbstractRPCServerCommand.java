package com.cell.cmd.impl;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.header.DefaultRPCHeader;
import com.cell.protocol.AbstractCommand;
import com.cell.protocol.ICommand;
import com.cell.protocol.IHead;
import com.cell.protocol.IServerRequest;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import com.cell.serialize.ISerializable;
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


    private Object newInstance(IRPCServerCommandContext commandContext, Class<?> bzClz) throws Exception
    {
        Object instance = null;
        IInputArchive inputArchive = this.getInputArchive(commandContext);
        if (ISerializable.class.isAssignableFrom(bzClz))
        {
            instance = bzClz.newInstance();
            ((ISerializable) instance).read(inputArchive);
        } else
        {
            instance = this.reflectFill(bzClz, inputArchive);
        }
        return instance;
    }

    protected IInputArchive getInputArchive(IRPCServerCommandContext commandContext) throws IOException
    {
        IServerRequest request = commandContext.getRequest();
        return JsonInput.createArchive(RPCUtils.readStringFromRequest(request));
    }

    @Override
    public void read(IInputArchive input) throws IOException
    {

    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {

    }
}
