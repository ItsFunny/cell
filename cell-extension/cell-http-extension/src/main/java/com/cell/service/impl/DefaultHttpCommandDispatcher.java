package com.cell.service.impl;

import com.cell.command.IHttpCommand;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.exception.HttpFramkeworkException;
import com.cell.hook.AbstractHttpCommandHook;
import com.cell.hook.HookCommandWrapper;
import com.cell.hook.HttpCommandHookResult;
import com.cell.hook.IHttpCommandHook;
import com.cell.hooks.IDeltaChainHook;
import com.cell.protocol.CommandContext;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 23:00
 */
@Data
public class DefaultHttpCommandDispatcher extends AbstractInitOnce implements IHttpCommandDispatcher, InitializingBean
{
    private volatile boolean ready;
    private short port;

    private IHttpCommandHook hook;


    private Map<String, Class<? extends IHttpCommand>> cmdMap = new HashMap<>();

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
        try
        {
            IHttpCommand cmd = this.getCmd(ctx);
            if (null == cmd)
            {
                ctx.discard();
                return;
            }
            HookCommandWrapper wp = new HookCommandWrapper();
            wp.setCtx(ctx);
            wp.setCmd(cmd);
            this.hook.hook(wp);
        } catch (Throwable e)
        {
            throw new HttpFramkeworkException(e.getMessage(), e);
        }

    }

    private IHttpCommand getCmd(CommandContext ctx) throws IllegalAccessException, InstantiationException
    {
        Class<? extends IHttpCommand> cmd = this.cmdMap.get(ctx.getURI());
        return cmd == null ? null : cmd.newInstance();
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

    private class cmdExecuteHook extends AbstractHttpCommandHook
    {

        @Override
        protected HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper)
        {
            return null;
        }

        @Override
        public IDeltaChainHook<HookCommandWrapper, HttpCommandHookResult> next()
        {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.hook.registerNext(new cmdExecuteHook());
    }
}
