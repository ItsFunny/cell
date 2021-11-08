package com.cell.discovery.nacos.http.extension;

import com.cell.Configuration;
import com.cell.annotations.CellOrder;
import com.cell.concurrent.BaseDefaultEventLoopGroup;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.configuration.RootConfiguration;
import com.cell.context.INodeContext;
import lombok.Data;

import java.util.Optional;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-01 09:16
 */
@CellOrder(value = Integer.MIN_VALUE)
public class ConcurrentExtension extends AbstractSpringNodeExtension
{
    private static EventLoopGroup eventLoopGroup;

    private static final String modulePath = "env.public.concurrent.json";

    public static EventLoopGroup getEventLoopGroup()
    {
        return eventLoopGroup;
    }

    @Data
    public static class ConcurrentConfiguration
    {
        private int threadCount = 256;
    }

    @Override
    public Object loadConfiguration(INodeContext ctx) throws Exception
    {
        ConcurrentConfiguration ret = null;
        try
        {
            ret = Configuration.getDefault().getConfigValue(modulePath).asObject(ConcurrentConfiguration.class);
        } catch (Exception e)
        {
            ret = new ConcurrentConfiguration();
        }
        return ret;
    }


    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        Optional<Object> configuration = RootConfiguration.getInstance().getExtensionConfiguration(ConcurrentExtension.class);
        ConcurrentConfiguration cfg = (ConcurrentConfiguration) configuration.get();
        int threadCount = cfg.getThreadCount();
        eventLoopGroup = new BaseDefaultEventLoopGroup(threadCount);
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
