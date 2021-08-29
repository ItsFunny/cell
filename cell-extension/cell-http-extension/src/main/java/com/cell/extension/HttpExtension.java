package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.context.INodeContext;
import com.cell.service.IDynamicControllerService;
import com.cell.service.impl.DynamicControllerServiceImpl;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:48
 */
public class HttpExtension extends AbstractSpringNodeExtension
{
    private IDynamicControllerService dynamicControllerService;

    @Plugin
    public IDynamicControllerService dynamicControllerService()
    {
        return new DynamicControllerServiceImpl();
    }

    @Override
    public void init(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void start(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }
}
