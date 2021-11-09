package com.cell.http.framework.bo;

import com.cell.base.core.constants.ContextConstants;
import com.cell.base.core.protocol.ContextResponseWrapper;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.http.framework.reactor.IMapDynamicHttpReactor;
import com.cell.http.framework.command.IHttpCommand;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 21:19
 */
@Data
public class BuzzContextBO
{
    private IHttpCommandContext context;
    private Object bo;
    private IHttpCommand cmd;

    public IMapDynamicHttpReactor getReactor()
    {
        return (IMapDynamicHttpReactor) this.context.getReactor();
    }

    public BuzzContextBO(IHttpCommandContext context, Object bo, IHttpCommand cmd)
    {
        this.context = context;
        this.bo = bo;
        this.cmd = cmd;
    }

    public ContextResponseWrapper.ContextResponseWrapperBuilder builder()
    {
        return ContextResponseWrapper.builder()
                .cmd(this.cmd);
    }

    public void success(Object ret)
    {
        this.context.response(this.builder()
                .status(ContextConstants.SUCCESS)
                .ret(ret)
                .build());
    }

    public void fail(Object ret)
    {
        this.context.response(this.builder()
                .cmd(this.cmd)
                .status(ContextConstants.FAIL)
                .ret(ret)
                .build());
    }

    public void fail(Object ret, Exception e)
    {
        this.context.response(
                this.builder()
                        .cmd(this.cmd)
                        .status(ContextConstants.FAIL)
                        .exception(e)
                        .ret(ret)
                        .build());
    }
}
