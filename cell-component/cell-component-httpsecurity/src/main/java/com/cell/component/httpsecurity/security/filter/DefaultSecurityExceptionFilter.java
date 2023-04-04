package com.cell.component.httpsecurity.security.filter;


import com.mi.wallet.mange.context.ContextUtils;
import com.mi.wallet.mange.context.IExceptionHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 2:17 下午
 */
public class DefaultSecurityExceptionFilter implements SecurityExceptionFilter
{
    private IExceptionHandler exceptionHandler;

    public DefaultSecurityExceptionFilter(IExceptionHandler exceptionHandler)
    {
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        try
        {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e)
        {
            this.exceptionHandler.handleException(ContextUtils.mustGetContext((HttpServletRequest) servletRequest), e);
        }
    }
}
