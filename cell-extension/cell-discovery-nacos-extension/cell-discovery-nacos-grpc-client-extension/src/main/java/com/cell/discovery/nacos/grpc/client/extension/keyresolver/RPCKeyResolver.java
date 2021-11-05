package com.cell.discovery.nacos.grpc.client.extension.keyresolver;

import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultStringKeyResolver;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 05:36
 */
public class RPCKeyResolver implements IKeyResolver<String, String>
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
