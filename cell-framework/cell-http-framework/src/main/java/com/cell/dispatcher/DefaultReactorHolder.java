package com.cell.dispatcher;

import com.cell.command.IHttpCommand;
import com.cell.reactor.IHttpReactor;
import io.netty.util.internal.ConcurrentSet;

import java.util.Map;
import java.util.Set;

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

    private static Set<IHttpReactor> reactors = new ConcurrentSet<>();


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
        reactors.add(reactor);
    }

    public static Set<IHttpReactor> getReactors()
    {
        return reactors;
    }
}
