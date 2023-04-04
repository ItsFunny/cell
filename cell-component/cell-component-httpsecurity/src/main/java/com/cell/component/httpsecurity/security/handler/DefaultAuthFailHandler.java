package com.cell.component.httpsecurity.security.handler;

import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.ContextUtils;
import com.mi.wallet.mange.context.IExceptionHandler;
import com.mi.wallet.mange.context.ModuleEnums;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthFailHandler extends DefaultFailHandler implements AuthFailHandler
{

    public DefaultAuthFailHandler(IExceptionHandler exceptionHandler)
    {
        super(exceptionHandler);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException
    {
        CellContext context = ContextUtils.mustGetContext(httpServletRequest);
        LOG.error(ModuleEnums.WEB_SECURITY, e, "鉴权失败,protocol={},sequenceId={}", context.getProtocolId(), context.getSequenceId());
        super.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
    }
}
