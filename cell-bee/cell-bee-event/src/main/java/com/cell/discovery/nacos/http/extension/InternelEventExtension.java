package com.cell.discovery.nacos.http.extension;

import com.cell.annotations.LifeCycle;
import com.cell.context.INodeContext;
import com.cell.enums.EnumLifeCycle;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 15:53
 */
@LifeCycle(lifeCycle = EnumLifeCycle.ONCE)
public class InternelEventExtension extends AbstractSpringNodeExtension
{
    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
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
