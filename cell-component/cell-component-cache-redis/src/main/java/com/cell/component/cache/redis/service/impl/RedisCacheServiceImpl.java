package com.cell.component.cache.redis.service.impl;

import com.cell.base.common.utils.StringUtils;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.INodeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisCacheServiceImpl implements ICacheService<String, String>
{
    @Autowired
    public void setTemplate(StringRedisTemplate template)
    {
        this.template = template;
    }

    private StringRedisTemplate template;


    @Override
    public void start(INodeContext context)
    {

    }

    @Override
    public String get(String s)
    {
        return this.template.opsForValue().get(s);
    }

    @Override
    public void set(String s, String s2, int delaySeconds)
    {
        this.template.opsForValue().set(s, s2, delaySeconds);
    }

    @Override
    public void set(String s, String s2)
    {
        this.template.opsForValue().set(s, s2);
    }

    @Override
    public String delete(String s)
    {
        String str = this.template.opsForValue().get(s);
        if (StringUtils.isNotEmpty(str))
        {
            this.template.delete(s);
        }
        return str;
    }

    @Override
    public boolean contains(String s)
    {
        return this.template.hasKey(s);
    }
}
