package com.cell.http.framework.reactor.impl;

import com.cell.base.common.context.InitCTX;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.common.utils.UriUtils;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.reactor.AbstractBaseCommandReactor;
import com.cell.base.core.utils.ClassUtil;
import com.cell.base.core.utils.ReflectUtil;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.constant.HttpConstants;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.http.framework.command.IHttpCommand;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//    protected Map<String, CommandWrapper> cmds = new HashMap<>(1);


    protected void done(HttpStatus status, Object ret)
    {
    }

    @Override
    public long getResultTimeout()
    {
        return HttpConstants.DEFAULT_RESULT_TIME_OUT;
    }

    @Data
    static class CommandWrapper
    {
        private HttpCmdAnno anno;
        private Class<? extends IHttpCommand> cmd;
    }

//    @Override
//    public void execute(IContext context)
//    {
//        DefaultHttpCommandContext ctx = (DefaultHttpCommandContext) context;
//
//        Class<? extends IHttpCommand> cmdClz = ctx.getCommand();
//        IHttpCommand cmd = null;
//        try
//        {
//            // FIXME optimize
//            cmd = cmdClz.newInstance();
//            ctx.setReactor(this);
//            cmd.execute(ctx);
//        } catch (Exception e)
//        {
//            ctx.response(this.createResponseWp()
//                    .status(ContextConstants.FAIL)
//                    .cmd(cmd)
//                    .exception(e)
//                    .build());
//        }
//    }


//    @Override
//    public List<Class<? extends IHttpCommand>> getHttpCommandList()
//    {
//        List<Class<? extends IHttpCommand>> ret = new ArrayList<>();
//        Collection<CommandWrapper> values = cmds.values();
//        for (CommandWrapper value : values)
//        {
//            ret.add(value.cmd);
//        }
//        return ret;
//    }


    @Override
    public void registerCmd(ICommand cmd)
    {
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.fillCmd(ctx);
        Class<? extends ICommand>[] cmds = this.getClass().getAnnotation(ReactorAnno.class).cmds();
        List<? extends Class<? extends IHttpCommand>> httpCommandList = Stream.of(cmds).map(c ->
                (Class<? extends IHttpCommand>) c).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(httpCommandList))
        {
            return;
        }
        httpCommandList.stream().forEach(p ->
                this.registerCmd((ICommand) ReflectUtil.newInstance(p)));
    }

    // FIXME ,这个需要删除
    private void fillCmd(InitCTX ctx)
    {
        Set<Class<? extends IHttpCommand>> httpCommandList = (Set<Class<? extends IHttpCommand>>) ctx.getData().get(HttpConstants.INIT_CTX_CMDS);
        ReactorAnno anno = ClassUtil.getMergedAnnotation(this.getClass(), ReactorAnno.class);
        String group = anno.group();
        if (CollectionUtils.isEmpty(httpCommandList)) return;

        httpCommandList.stream().forEach(c ->
        {
            HttpCmdAnno annotation = c.getAnnotation(HttpCmdAnno.class);
            if (annotation == null)
            {
                throw new ProgramaException("asd");
            }
            // FIXME
            final String urlStr = UriUtils.mergeUri(group, annotation.uri());
            try
            {
                new URL("http:127.0.0.1:8080" + urlStr);
            } catch (MalformedURLException e)
            {
                throw new ProgramaException("url不合法:" + urlStr);
            }
            ReflectUtil.modify(c, HttpCmdAnno.class, "uri", urlStr);
        });
        List<Class<? extends IHttpCommand>> cmds = new ArrayList<>(httpCommandList);
        ReflectUtil.modify(this.getClass(), ReactorAnno.class, "cmds", cmds.toArray(new Class<?>[cmds.size()]));
    }
}
