package com.cell.command.impl;

import com.cell.annotation.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.protocol.AbstractCommand;
import com.cell.protocol.IBuzzContext;
import com.cell.protocol.ICommand;
import com.cell.protocol.IHead;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.JsonInput;
import com.cell.util.HttpUtils;
import com.cell.utils.ClassUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
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



    @Override
    protected IInputArchive getInputArchiveFromCtx(IBuzzContext c) throws Exception
    {
        return this.getInputArchive((IHttpCommandContext) c);
    }


    @Override
    protected void doExecute(IBuzzContext ctx, Object bo) throws IOException
    {
        this.onExecute((IHttpCommandContext) ctx, bo);
    }


    private IInputArchive getInputArchive(IHttpCommandContext commandContext) throws IOException
    {
        EnumHttpRequestType requestType = this.httpCmdAnno.requestType();
        HttpServletRequest httpRequest = commandContext.getHttpRequest();
        String jsonStr;
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
