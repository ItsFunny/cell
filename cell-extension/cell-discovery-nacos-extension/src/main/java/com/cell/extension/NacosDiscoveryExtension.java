package com.cell.extension;

import com.cell.annotations.AutoPlugin;
import com.cell.context.INodeContext;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.service.INodeDiscovery;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:06
 */

public class NacosDiscoveryExtension extends AbstractSpringNodeExtension
{
    @AutoPlugin
    private IHttpCommandDispatcher dispatcher;

    private INodeDiscovery nodeDiscovery;

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
