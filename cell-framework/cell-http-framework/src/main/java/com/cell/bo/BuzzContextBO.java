package com.cell.bo;

import com.cell.grpc.client.base.framework.command.IHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.reactor.IMapDynamicHttpReactor;
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

    public IMapDynamicHttpReactor getReactor(){
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
