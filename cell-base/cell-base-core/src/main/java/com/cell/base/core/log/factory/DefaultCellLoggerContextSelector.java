package com.cell.base.core.log.factory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-17 23:36
 */
public class DefaultCellLoggerContextSelector implements ContextSelector
{
    private static LoggerContext defaultLoggerContext = null;

    public DefaultCellLoggerContextSelector(LoggerContext context)
    {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(context.getClass());
        enhancer.setCallback(new LogMethodProxy());
        this.defaultLoggerContext = (LoggerContext) enhancer.create();
    }

    static class LogMethodProxy implements MethodInterceptor
    {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable
        {
            String methodName = method.getName();
//            System.out.println(method.getName());
            if (methodName.contains("getLogger"))
            {
                if (null != objects && objects.length > 0)
                {
                    return DefaultSlf4jLoggerFactory.getInstance().getLogger((String) objects[0]);
                }
                return DefaultSlf4jLoggerFactory.getInstance().getConsoleLogger();
            }
            return methodProxy.invokeSuper(o, objects);
        }
    }

    public LoggerContext getLoggerContext()
    {
        return this.getDefaultLoggerContext();
    }

    public LoggerContext getDefaultLoggerContext()
    {
        return this.defaultLoggerContext;
    }

    public LoggerContext detachLoggerContext(String loggerContextName)
    {
        return this.defaultLoggerContext;
    }

    public List<String> getContextNames()
    {
        return Arrays.asList(this.defaultLoggerContext.getName());
    }

    public LoggerContext getLoggerContext(String name)
    {
        return this.defaultLoggerContext.getName().equals(name) ? this.defaultLoggerContext : null;
    }
}
