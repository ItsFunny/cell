package com.cell.http.framework.command.impl;

import com.cell.bo.BuzzContextBO;
import com.cell.context.IHttpCommandContext;
import com.cell.http.framework.command.IBuzzExecutor;

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


    @Override
    protected void onExecute(IHttpCommandContext ctx, Object bo)
    {
        BuzzContextBO reqBO = new BuzzContextBO(ctx, bo, this);
        this.buzzExecutor.execute(reqBO);
    }
}
