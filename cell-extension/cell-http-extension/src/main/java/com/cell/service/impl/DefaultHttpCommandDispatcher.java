package com.cell.service.impl;

import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.config.AbstractInitOnce;
import com.cell.context.DefaultHttpCommandContext;
import com.cell.context.InitCTX;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.exception.HttpFramkeworkException;
import com.cell.exceptions.ProgramaException;
import com.cell.hook.HookCommandWrapper;
import com.cell.hook.HttpCommandHookResult;
import com.cell.hook.IHttpCommandHook;
import com.cell.protocol.CommandContext;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
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
    private short port = 8080;

    private IHttpCommandHook tracker;

    private Map<String, Class<? extends IHttpCommand>> cmdMap = new HashMap<>();
    private Map<String, IHttpReactor> reactorMap = new HashMap<>();

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
            DefaultHttpCommandContext commandContext = new DefaultHttpCommandContext(ctx, tracker);
            IHttpReactor reactor = this.getReactor(ctx.getURI());
            if (null == reactor)
            {
                commandContext.discard();
                return;
            }

            HookCommandWrapper wp = new HookCommandWrapper();
            wp.setContext(commandContext);
            wp.setReactor(reactor);
            HttpCommandHookResult httpCommandHookResult = this.tracker.trackBegin(wp);
            this.tracker.trackEnd(httpCommandHookResult);
        } catch (Throwable e)
        {
            throw new HttpFramkeworkException(e.getMessage(), e);
        }

    }

    @Override
    public void addReactor(IHttpReactor reactor)
    {
        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
        if (annotation == null)
        {
            throw new ProgramaException("reactor annotation必须有 @ReactorAnno 注解 ");
        }
        List<Class<? extends IHttpCommand>> clist = reactor.getHttpCommandList();
        for (Class<? extends IHttpCommand> cc : clist)
        {
            HttpCmdAnno anno = (HttpCmdAnno) ClassUtil.getAnnotation(cc, HttpCmdAnno.class);
            if (this.cmdMap.containsKey(anno.uri()))
            {
                throw new ProgramaException(String.format("duplicated command %s uri %s", cc, anno.uri()));
            }
            this.cmdMap.put(anno.uri(), cc);
            this.reactorMap.put(anno.uri(), reactor);
        }
    }

//    private IHttpCommand getCmd(CommandContext ctx) throws IllegalAccessException, InstantiationException
//    {
//        Class<? extends IHttpCommand> cmd = this.cmdMap.get(ctx.getURI());
//        if (cmd == null)
//        {
//            return null;
//        }
//        IHttpReactor reactor = this.reactorMap.get(ctx.getURI());
//        if (null == reactor)
//        {
//            throw new ProgramaException("asd");
//        }
//        IHttpCommand ret = cmd.newInstance();
//        ret.setReactor(reactor);
//        DefaultHttpCommandContext commandContext = new DefaultHttpCommandContext(ctx);
//        ret.setCtx(commandContext);
//        return ret;
//    }

    private IHttpReactor getReactor(String uri)
    {
        return this.reactorMap.get(uri);
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


    @Override
    public void afterPropertiesSet() throws Exception
    {

    }
}
