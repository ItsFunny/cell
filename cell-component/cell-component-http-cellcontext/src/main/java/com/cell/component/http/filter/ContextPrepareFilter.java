package com.cell.component.http.filter;

import com.cell.base.common.models.Module;
import com.cell.component.http.filter.manager.FilterManager;
import com.cell.http.framework.filter.CellFilter;
import com.cell.http.framework.util.ContextUtils;
import com.cell.node.core.context.CellContext;
import com.cell.sdk.log.LOG;
import com.google.common.base.Stopwatch;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Data
public class ContextPrepareFilter implements CellFilter
{
    @Autowired(required = false)
    private ICellExceptionHandler exceptionHandler;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        // TODO ,config
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        CellContext ctx = ContextUtils.prepareContext((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        LOG.info(Module.HTTP_FRAMEWORK, "收到api请求,{}", ctx.toString());
        Stopwatch stopwatch = Stopwatch.createStarted();
        try
        {
            FilterManager.dispatch(ctx);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e)
        {
            if (null != this.exceptionHandler)
            {
                this.exceptionHandler.handleException(ctx, e);
            } else
            {
                throw e;
            }
        } finally
        {
            long cost = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            LOG.info(Module.HTTP_FRAMEWORK, "结束api请求,{},cost:{}ms", ctx.toString(), cost);
        }
    }
}