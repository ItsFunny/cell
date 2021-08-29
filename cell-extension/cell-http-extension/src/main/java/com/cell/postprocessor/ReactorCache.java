package com.cell.postprocessor;

import com.cell.reactor.IDynamicHttpReactor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 07:35
 */
public class ReactorCache
{
    private static final Map<Class<?>, IDynamicHttpReactor> REACTOR_MAP = new ConcurrentHashMap<>();

    public static void register(Class<?> c, IDynamicHttpReactor h)
    {
        REACTOR_MAP.putIfAbsent(c, h);
    }

    public static Collection<IDynamicHttpReactor>getReactors(){
        return REACTOR_MAP.values();
    }
}
