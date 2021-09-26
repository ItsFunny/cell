package com.cell.reactor.impl;

import com.cell.annotation.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.constant.HttpConstants;
import com.cell.constants.ContextConstants;
import com.cell.context.DefaultHttpCommandContext;
import com.cell.context.InitCTX;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exceptions.ProgramaException;
import com.cell.models.Module;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;
import com.cell.reactor.AbstractBaseCommandReactor;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.*;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
        Class<? extends IHttpCommand> cmdClz = ctx.getCommand();
        IHttpCommand cmd = null;
        try
        {
            // FIXME optimize
            cmd = cmdClz.newInstance();
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
    public void registerCmd(ICommand cmd)
    {
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
                public Class<?> buzzClz()
                {
                    return annotation.buzzClz();
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
