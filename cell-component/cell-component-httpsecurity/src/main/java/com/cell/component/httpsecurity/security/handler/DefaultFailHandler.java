package com.cell.component.httpsecurity.security.handler;

import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.ContextUtils;
import com.mi.wallet.mange.context.IExceptionHandler;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultFailHandler implements FailureHandler
{
    public DefaultFailHandler(IExceptionHandler exceptionHandler)
    {
        this.handler = exceptionHandler;
    }

    private IExceptionHandler handler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException
    {
        Throwable cause = e.getCause();
        CellContext ctx = ContextUtils.mustGetContext(httpServletRequest);
        if (cause == null)
        {
            this.handler.handleException(ctx, e);
        } else
        {
            this.handler.handleException(ctx, cause);
        }
    }
}
