package com.cell.command.impl;

import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.AbstractCommand;
import com.cell.protocol.ICommand;
import com.cell.protocol.IHead;
import com.cell.reactor.IHttpReactor;
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
    public void discard() throws IOException
    {
        this.getCtx().discard();
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
}
