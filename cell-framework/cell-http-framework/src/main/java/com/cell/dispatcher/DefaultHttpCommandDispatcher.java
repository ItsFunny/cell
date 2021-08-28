package com.cell.dispatcher;

import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.exception.HttpFramkeworkException;
import com.cell.hook.IHttpCommandHook;
import com.cell.protocol.CommandContext;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 23:00
 */
public class DefaultHttpCommandDispatcher extends AbstractInitOnce implements IHttpCommandDispatcher
{
    private volatile boolean ready;
    private short port;

    private IHttpCommandHook hook;

    @Override
    public short getPort()
    {
        return this.port;
    }

    @Override
    public void setPort(short port)
    {
        this.port = port;
    }

    @Override
    public void dispath(CommandContext ctx) throws HttpFramkeworkException
    {
        
    }

    @Override
    public boolean ready()
    {
        return ready;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.ready = true;
    }
}
