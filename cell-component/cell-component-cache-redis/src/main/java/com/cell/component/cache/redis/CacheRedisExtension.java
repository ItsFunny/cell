package com.cell.component.cache.redis;

import com.cell.component.cache.redis.config.RedisConfig;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

public class CacheRedisExtension extends AbstractSpringNodeExtension
{


    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        RedisConfig.getInstance().seal(ctx);
    }

    @Override
    public boolean isRequired()
    {
        return false;
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
