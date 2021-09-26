package com.cell.dispatcher;

import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.constant.HttpConstants;
import com.cell.context.InitCTX;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ClassUtil;

import java.util.Collection;
import java.util.HashMap;
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
// FIXME too stupid & need clean
public class DefaultReactorHolder
{
    private static IHttpCommandDispatcher instance = null;

    private static Map<Class<? extends IHttpReactor>, IHttpReactor> reactors = new HashMap<>();

    private static Map<Class<? extends IHttpReactor>, Set<Class<? extends IHttpCommand>>> commands;


    public static void setDispatcher(IHttpCommandDispatcher d)
    {
        instance = d;
    }

    public static IHttpCommandDispatcher getInstance()
    {
        return instance;
    }

    public static void setCommands(Map<Class<? extends IHttpReactor>, Set<Class<? extends IHttpCommand>>> cmds)
    {
        commands = cmds;
    }

    // FIXME ,FLUSH
    public static void addReactor(IHttpReactor reactor)
    {
        String name = reactor.getClass().getName();
        InitCTX ctx = new InitCTX();
        if (!name.contains("ByteBuddy"))
        {
            Map<String, Object> data = new HashMap<>();
            Set<Class<? extends IHttpCommand>> cmdList = commands.get(reactor.getClass());
            data.put(HttpConstants.INIT_CTX_CMDS, cmdList);
            ctx.setData(data);
        }
        reactor.initOnce(ctx);
        synchronized (reactors)
        {
            Class<? extends IHttpReactor> aClass = reactor.getClass();
            ReactorAnno anno = (ReactorAnno) ClassUtil.getAnnotation(aClass, ReactorAnno.class);
            if (!reactors.containsKey(aClass) || anno.withForce().forceOverride())
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
