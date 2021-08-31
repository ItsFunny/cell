package com.cell.dispatcher;

import com.cell.annotations.ForceOverride;
import com.cell.command.IHttpCommand;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 22:47
 */
// FIXME too stupid
public class DefaultReactorHolder
{
    private static IHttpCommandDispatcher instance = null;

    private static Map<Class<? extends IHttpReactor>, IHttpReactor> reactors = new HashMap<>();


    public static void setDispatcher(IHttpCommandDispatcher d)
    {
        instance = d;
    }

    public static IHttpCommandDispatcher getInstance()
    {
        return instance;
    }

    public static void addReactor(IHttpReactor reactor)
    {
        reactor.initOnce(null);
        synchronized (reactors)
        {
            Class<? extends IHttpReactor> aClass = reactor.getClass();
            if (!reactors.containsKey(aClass) || ClassUtil.hasAnnotation(aClass, ForceOverride.class))
            {
                reactors.put(aClass, reactor);
            }
        }
    }

    public static Collection<IHttpReactor> getReactors()
    {
        return reactors.values();
    }
}
