package com.cell.component.httpsecurity.security.filter;

import com.cell.sdk.log.LOG;
import com.google.common.base.Stopwatch;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.ContextUtils;
import com.mi.wallet.mange.context.ModuleEnums;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ContextPrepareFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        CellContext ctx = ContextUtils.prepareContext((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
//        servletRequest.setAttribute(AppInterceptors.REQUEST_TIME, new Date());
        LOG.info(ModuleEnums.WEB_SECURITY, "收到api请求,{}", ctx.toString());
        Stopwatch stopwatch = Stopwatch.createStarted();
        try
        {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally
        {
            long cost = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            LOG.info(ModuleEnums.WEB_SECURITY, "结束api请求,{},cost:{}ms", ctx.toString(), cost);
        }
    }
}
