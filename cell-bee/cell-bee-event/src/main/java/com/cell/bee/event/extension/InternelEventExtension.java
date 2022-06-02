package com.cell.bee.event.extension;

import com.cell.base.core.annotations.LifeCycle;
import com.cell.base.core.enums.EnumLifeCycle;
import com.cell.bee.event.center.EventCenter;
import com.cell.bee.event.center.JobCenter;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

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
        JobCenter.getInstance().registerSubscriber(EventCenter.getInstance());
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
