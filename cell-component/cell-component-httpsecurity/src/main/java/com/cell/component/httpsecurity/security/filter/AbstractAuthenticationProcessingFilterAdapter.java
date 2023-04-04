package com.cell.component.httpsecurity.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationProcessingFilterAdapter extends AbstractAuthenticationProcessingFilter
{

    protected AbstractAuthenticationProcessingFilterAdapter(String defaultFilterProcessesUrl)
    {
        super(defaultFilterProcessesUrl);
    }

    protected AbstractAuthenticationProcessingFilterAdapter(RequestMatcher requiresAuthenticationRequestMatcher)
    {
        super(requiresAuthenticationRequestMatcher);
    }

    protected AbstractAuthenticationProcessingFilterAdapter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager)
    {
        super(defaultFilterProcessesUrl, authenticationManager);
    }


    protected AbstractAuthenticationProcessingFilterAdapter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager)
    {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException
    {
        this.getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}


