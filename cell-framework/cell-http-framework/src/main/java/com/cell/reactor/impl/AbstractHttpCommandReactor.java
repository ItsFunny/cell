package com.cell.reactor.impl;

import com.cell.App;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.DefaultHttpCommandContext;
import com.cell.context.IHttpContext;
import com.cell.exceptions.CommandException;
import com.cell.exceptions.ProgramaException;
import com.cell.protocol.CommandContext;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;
import com.cell.reactor.AbstractBaseCommandReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.reactor.IReactor;
import com.cell.utils.ClassUtil;
import lombok.Data;
import org.reflections.util.ConfigurationBuilder;

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
public abstract class AbstractHttpCommandReactor extends AbstractBaseCommandReactor implements IHttpReactor
{
    protected Map<String, CommandWrapper> cmds = new HashMap<>(1);

    @Data
    static class CommandWrapper
    {
        private HttpCmdAnno anno;
        private Class<? extends IHttpCommand> cmd;
    }

    @Override
    public void execute(IContext context)
    {
        DefaultHttpCommandContext ctx = (DefaultHttpCommandContext) context;
        String uri = ctx.getURI();
        CommandWrapper wp = this.cmds.get(uri);
        if (wp == null)
        {
            context.response(this.createResponseWp()
                    .status(ContextConstants.PROGRAMA_ERROR)
                    .msg("该commd不存在:" + uri)
                    .build());
            return;
        }

        IHttpCommand cmd = null;
        try
        {
            // FIXME optimize
            cmd = wp.getCmd().newInstance();
            ctx.setCmd(cmd);
            ctx.setReactor(this);
            cmd.execute(ctx);
        } catch (Exception e)
        {
            context.response(this.createResponseWp()
                    .status(ContextConstants.FAIL)
                    .cmd(cmd)
                    .exception(e)
                    .build());
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
