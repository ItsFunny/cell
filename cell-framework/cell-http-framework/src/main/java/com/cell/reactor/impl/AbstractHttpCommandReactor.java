package com.cell.reactor.impl;

import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.constant.HttpConstants;
import com.cell.constants.ContextConstants;
import com.cell.context.DefaultHttpCommandContext;
import com.cell.context.HttpContextResponseBody;
import com.cell.context.InitCTX;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exception.HttpFramkeworkException;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;
import com.cell.reactor.AbstractBaseCommandReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ReflectUtil;
import com.cell.utils.StringUtils;
import lombok.Data;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.springframework.http.HttpStatus;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cell.utils.ClassUtil.mustGetAnnotation;

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

    protected ContextResponseWrapper.ContextResponseWrapperBuilder createResponseWp()
    {
        return ContextResponseWrapper.builder();
    }


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

    @Override
    public void execute(IContext context)
    {
        DefaultHttpCommandContext ctx = (DefaultHttpCommandContext) context;
        String uri = ctx.getURI();
        CommandWrapper wp = this.cmds.get(uri);
        if (wp == null)
        {
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.PROGRAMA_ERROR)
                    .other(HttpContextResponseBody.builder().status(HttpStatus.NOT_FOUND).build())
                    .exception(new HttpFramkeworkException("cmd不存在", "asd"))
                    .msg("该commd不存在:" + uri)
                    .build());
            return;
        }

        IHttpCommand cmd = null;
        try
        {
            // FIXME optimize
            cmd = wp.getCmd().newInstance();
            ctx.setReactor(this);
            cmd.execute(ctx);
        } catch (Exception e)
        {
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.FAIL)
                    .cmd(cmd)
                    .exception(e)
                    .build());
        }
    }


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
    public Class<? extends IHttpCommand> getCmd(String uri)
    {
        if (!this.cmds.containsKey(uri))
        {
            return null;
        }
        return this.cmds.get(uri).getCmd();
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
        LOG.info(Module.HTTP_FRAMEWORK, "mapping uri:{}", anno.uri());
        CommandWrapper wp = new CommandWrapper();
        wp.setAnno(anno);
        wp.setCmd((Class<? extends IHttpCommand>) cmd.getClass());
        this.cmds.put(anno.uri(), wp);
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.fillCmd();
        List<Class<? extends IHttpCommand>> httpCommandList = this.getHttpCommandList();
        if (CollectionUtils.isEmpty(httpCommandList))
        {
            return;
        }
        httpCommandList.stream().forEach(p ->
                this.registerCmd((ICommand) ReflectUtil.newInstance(p)));
    }

    // FIXME ,这个需要删除
    private void fillCmd()
    {
        List<Class<? extends IHttpCommand>> httpCommandList = this.getHttpCommandList();
        ReactorAnno anno = (ReactorAnno) ClassUtil.mustGetAnnotation(this.getClass(), ReactorAnno.class);
        String group = anno.group();
        if (StringUtils.isEmpty(group)) return;
        httpCommandList.stream().forEach(c ->
        {
            HttpCmdAnno annotation = c.getAnnotation(HttpCmdAnno.class);
            if (annotation == null)
            {
                throw new ProgramaException("asd");
            }
            // FIXME
            final String urlStr = group + annotation.uri();
            try
            {
                new URL("http:127.0.0.1:8080" + urlStr);
            } catch (MalformedURLException e)
            {
                throw new ProgramaException("url不合法:" + urlStr);
            }
//            new ByteBuddy().redefine(c)
//                    .annotateType(AnnotationDescription.Builder.ofType(HttpCmdAnno.class)
//                            .define("requestType", annotation.requestType())
//                            .define("responseType", annotation.responseType())
//                            .define("uri", urlStr)
//                            .define("httpCommandId", annotation.httpCommandId())
//                            .define("viewName", annotation.viewName())
//                            .define("group", annotation.group())
//                            .define("websocket", annotation.websocket())
//                            .build())
//                    .make()
//                    .load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            final HttpCmdAnno newAnno = new HttpCmdAnno()
            {
                @Override
                public EnumHttpRequestType requestType()
                {
                    return annotation.requestType();
                }

                @Override
                public EnumHttpResponseType responseType()
                {
                    return annotation.responseType();
                }

                @Override
                public String uri()
                {
                    return urlStr;
                }

                @Override
                public Module module()
                {
                    return annotation.module();
                }

                @Override
                public short httpCommandId()
                {
                    return annotation.httpCommandId();
                }

                @Override
                public String viewName()
                {
                    return annotation.viewName();
                }

                @Override
                public String group()
                {
                    return annotation.group();
                }

                @Override
                public boolean websocket()
                {
                    return annotation.websocket();
                }

                @Override
                public Class<? extends Annotation> annotationType()
                {
                    return annotation.annotationType();
                }
            };
            ReflectUtil.modify(c, HttpCmdAnno.class, "uri", urlStr);
        });
    }
}
