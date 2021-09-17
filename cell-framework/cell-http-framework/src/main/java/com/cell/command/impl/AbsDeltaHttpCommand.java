package com.cell.command.impl;

import com.cell.bo.BuzzContextBO;
import com.cell.command.IBuzzExecutor;
import com.cell.context.IHttpCommandContext;
import com.cell.serialize.ISerializable;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 19:09
 */
public abstract class AbsDeltaHttpCommand extends AbstractHttpCommand
{
    private IBuzzExecutor buzzExecutor;

    public AbsDeltaHttpCommand()
    {
        super();
        this.buzzExecutor = this.setUpBuzzExecutor();
    }

    protected abstract IBuzzExecutor setUpBuzzExecutor();

//    @Override
//    public ISerializable getBO(IContext context)
//    {
//        return this.buzzExecutor.serialize((IHttpContext) context);
//    }

    @Override
    protected void onExecute(IHttpCommandContext ctx, ISerializable bo) throws IOException
    {
        BuzzContextBO reqBO = new BuzzContextBO(ctx, bo, this);
        this.buzzExecutor.execute(reqBO);
    }
}
