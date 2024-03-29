//package com.cell.http.framework.filter;
//
//import com.cell.base.common.models.Module;
//import com.cell.http.framework.context.CellContext;
//import com.cell.http.framework.util.ContextUtils;
//import com.cell.sdk.log.LOG;
//import com.google.common.base.Stopwatch;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//public class ContextPrepareFilter implements CellFilter
//{
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
//    {
//        CellContext ctx = ContextUtils.prepareContext((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
//        LOG.info(Module.HTTP_FRAMEWORK, "收到api请求,{}", ctx.toString());
//        Stopwatch stopwatch = Stopwatch.createStarted();
//        try
//        {
//            filterChain.doFilter(servletRequest, servletResponse);
//        } finally
//        {
//            long cost = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
//            LOG.info(Module.HTTP_FRAMEWORK, "结束api请求,{},cost:{}ms", ctx.toString(), cost);
//        }
//    }
//}