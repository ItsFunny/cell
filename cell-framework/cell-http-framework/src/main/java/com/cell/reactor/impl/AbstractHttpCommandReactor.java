package com.cell.reactor.impl;

import com.cell.App;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.context.DefaultHttpCommandContext;
import com.cell.context.IHttpContext;
import com.cell.exceptions.CommandException;
import com.cell.exceptions.ProgramaException;
import com.cell.protocol.CommandContext;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;
import lombok.Data;

import java.io.IOException;
import java.util.*;

import static com.cell.utils.ClassUtil.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 14:24
 */
public abstract class AbstractHttpCommandReactor implements IHttpReactor
{
    protected Map<String, CommandWrapper> cmds = new HashMap<>(1);

    @Data
    static class CommandWrapper
    {
        private HttpCmdAnno anno;
        private Class<? extends IHttpCommand> cmd;
    }

    @Override
    public void execute(IContext context) throws CommandException
    {
        DefaultHttpCommandContext ctx = (DefaultHttpCommandContext) context;
        String uri = ctx.getURI();
        CommandWrapper wp = this.cmds.get(uri);
        if (wp == null)
        {
            // 目的: 所有的
            try
            {
                context.discard();
            } catch (IOException e)
            {
                throw new CommandException(e);
            }
        }
        
        try
        {
            // FIXME optimize
            IHttpCommand cmd = wp.getCmd().newInstance();
            ctx.setCmd(cmd);
            cmd.setReactor(this);
            cmd.setCtx(ctx);
            cmd.execute();
        } catch (Exception e)
        {
            throw new CommandException(e);
        }
    }

    @Override
    public List<Class<? extends IHttpCommand>> getHttpCommandList()
    {
        List<Class<? extends IHttpCommand>> ret = new ArrayList<>();
        Collection<CommandWrapper> values = cmds.values();
        for (CommandWrapper value : values)
        {
            ret.add(value.cmd);
        }
        return ret;
    }

    @Override
    public void registerCmd(ICommand cmd)
    {
        if (!(cmd instanceof IHttpCommand))
        {
            throw new ProgramaException("zz");
        }
        HttpCmdAnno anno = (HttpCmdAnno) mustGetAnnotation(cmd.getClass(), HttpCmdAnno.class);
        if (this.cmds.containsKey(anno.uri()))
        {
            throw new ProgramaException("aaa");
        }
        CommandWrapper wp = new CommandWrapper();
        wp.setAnno(anno);
        wp.setCmd((Class<? extends IHttpCommand>) cmd.getClass());
        this.cmds.put(anno.uri(), wp);
    }
}
