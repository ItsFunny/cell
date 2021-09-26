package com.cell.command.impl;

import com.cell.annotation.HttpCmdAnno;
import com.cell.annotations.Optional;
import com.cell.command.IHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exceptions.InternalWrapperException;
import com.cell.protocol.*;
import com.cell.reactor.IHttpReactor;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import com.cell.serialize.ISerializable;
import com.cell.serialize.JsonInput;
import com.cell.util.HttpUtils;
import com.cell.utils.ClassUtil;
import lombok.Data;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:22
 */
@Data
public abstract class AbstractHttpCommand extends AbstractCommand implements IHttpCommand
{
    protected HttpCmdAnno httpCmdAnno;
    protected String mov;
    protected IHttpReactor reactor;

    public AbstractHttpCommand()
    {
        super();
        this.httpCmdAnno = (HttpCmdAnno) ClassUtil.getAnnotation(this.getClass(), HttpCmdAnno.class);
    }

    @Override
    protected IHead newHead()
    {
        HttpHeader httpHeader = new HttpHeader(this);
        return httpHeader;
    }

    @Override
    protected void onMakeCouple(ICommand f)
    {
        IHttpCommand couple = (IHttpCommand) f;
    }

    @Override
    public String modelAndView()
    {
        return this.mov;
    }

    @Override
    public EnumHttpRequestType getRequestType()
    {
        return this.httpCmdAnno.requestType();
    }

    @Override
    public EnumHttpResponseType getResponseType()
    {
        return this.httpCmdAnno.responseType();
    }

    protected abstract void onExecute(IHttpCommandContext ctx, Object o) throws IOException;

    protected IHttpCommandContext getHttpContext()
    {
        return (IHttpCommandContext) this.getCtx();
    }

    // 解析参数
    @Override
    public void read(IInputArchive input) throws IOException
    {

    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {

    }

    protected ContextResponseWrapper.ContextResponseWrapperBuilder createResponseWp()
    {
        return this.baseComdResponseWrapper();
    }

    @Override
    public void execute(IBuzzContext ctx)
    {
        try
        {
            Class<?> bzClz = this.httpCmdAnno.buzzClz();
            Object bo = null;
            if (bzClz != Void.class)
            {
                bo = this.newInstance((IHttpCommandContext) ctx, bzClz);
            }
            this.onExecute((IHttpCommandContext) ctx, bo);
        } catch (Exception e)
        {
            throw new InternalWrapperException(e);
        }
    }

    private Object newInstance(IHttpCommandContext commandContext, Class<?> bzClz) throws Exception
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

    private Object reflectFill(Class<?> clz, IInputArchive inputArchive) throws IOException
    {
        Field[] fields = clz.getDeclaredFields();
        BeanWrapper beanWrapper = new BeanWrapperImpl(clz);
        for (Field field : fields)
        {
            String name = field.getName();
            if (name.startsWith("abs")) continue;
            Optional annotation = field.getAnnotation(Optional.class);
            if (annotation == null)
            {
                beanWrapper.setPropertyValue(name, inputArchive.readString(name));
            } else
            {
                beanWrapper.setPropertyValue(name, inputArchive.readStringNullable(name));
            }
        }
        return beanWrapper.getWrappedInstance();
    }

    private IInputArchive getInputArchive(IHttpCommandContext commandContext) throws IOException
    {
        EnumHttpRequestType requestType = this.httpCmdAnno.requestType();
        HttpServletRequest httpRequest = commandContext.getHttpRequest();
        String jsonStr = null;
        if (requestType.equals(EnumHttpRequestType.HTTP_POST))
        {
            jsonStr = HttpUtils.readStringFromPostRequest(httpRequest);
        } else
        {
            jsonStr = HttpUtils.fromHttpRequest(httpRequest);
        }
        return JsonInput.createArchive(jsonStr);
    }
}
