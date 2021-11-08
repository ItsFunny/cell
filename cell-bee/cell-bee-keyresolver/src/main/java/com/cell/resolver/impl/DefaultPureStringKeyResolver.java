package com.cell.resolver.impl;

import com.cell.resolver.IKeyResolver;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 09:38
 */
public class DefaultPureStringKeyResolver implements IKeyResolver<String, String>
{
    @Override
    public String resolve(String t)
    {
        return t;
    }

    @Override
    public boolean match(String res, String req)
    {
        return res.equalsIgnoreCase(req);
    }

    @Override
    public String before(String s)
    {
        return s;
    }
}
