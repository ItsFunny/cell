package com.cell.component.httpsecurity.security.filter;

import com.mi.wallet.mange.context.*;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 权限校验url,拦截需要权限校验的路径, 自动校验url是否有权限访问,操作是否有数据权限等
public class AuthAuthenticationFilter extends AbstractAuthenticationProcessingFilterAdapter
{
    private IAuthRuleEngine authExecutor;


    public AuthAuthenticationFilter(IAuthRuleEngine authExecutor,
                                    RequestMatcher requiresAuthenticationRequestMatcher,
                                    AuthenticationSuccessHandler successHandler,
                                    AuthenticationFailureHandler failureHandler
    )
    {
        super(requiresAuthenticationRequestMatcher);
        this.authExecutor = authExecutor;
        this.setContinueChainBeforeSuccessfulAuthentication(true);
        this.setAuthenticationFailureHandler(failureHandler);
        this.setAuthenticationSuccessHandler(successHandler);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException
    {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CellContext ctx = ContextUtils.mustGetContext(httpServletRequest);
        if (!this.authExecutor.analyse(ctx, authentication))
        {
            throw new AuthenticationServiceException("权限校验失败", new WrapContextException(ContextUtils.mustGetContext(httpServletRequest), new BusinessException(ErrorConstant.NOT_AUTHORIZATION)));
        }
        return authentication;
    }
}
