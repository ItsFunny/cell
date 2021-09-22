package com.cell.command.impl;

import com.cell.annotations.HttpCmdAnno;
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
import com.cell.utils.ClassUtil;
import lombok.Data;

import java.io.IOException;

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

    protected abstract void onExecute(IHttpCommandContext ctx, ISerializable bo) throws IOException;

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
            this.onExecute((IHttpCommandContext) ctx, this.getBO(ctx));
        } catch (IOException e)
        {
            throw new InternalWrapperException(e);
        }
    }
}
