package com.cell.extension;

import com.cell.annotations.LifeCycle;
import com.cell.annotations.Plugin;
import com.cell.context.INodeContext;
import com.cell.enums.EnumLifeCycle;
import com.cell.impl.StatContextImpl;
import com.cell.services.IStatContextService;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:27
 */
@LifeCycle(lifeCycle = EnumLifeCycle.ONCE)
public  class BaseMetricsExtension extends AbstractSpringNodeExtension
{
    private IStatContextService service;

    @Plugin
    public IStatContextService statContextService()
    {
        return this.service;
    }


    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        this.service = new StatContextImpl(ctx);
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
