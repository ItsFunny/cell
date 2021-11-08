package com.cell.bee.event.simple;

import com.cell.plugin.pipeline.manager.IReflectManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 09:55
 */
public class DefaultSimpleEvent extends AbstractSimpleEventCenter
{

    @Override
    protected void afterInvoke()
    {

    }


    @Override
    public IReflectManager createOrDefault()
    {
        throw new RuntimeException("not supposed yet");
    }
}
