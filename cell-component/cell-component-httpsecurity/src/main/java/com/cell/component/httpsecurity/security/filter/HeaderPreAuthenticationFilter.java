package com.cell.component.httpsecurity.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 1:45 下午
 */
public abstract class HeaderPreAuthenticationFilter extends AbstractAuthenticationProcessingFilter
{

    protected HeaderPreAuthenticationFilter(String defaultFilterProcessesUrl)
    {
        super(defaultFilterProcessesUrl);
    }

    protected HeaderPreAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher)
    {
        super(requiresAuthenticationRequestMatcher);
    }

    protected HeaderPreAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager)
    {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    protected HeaderPreAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager)
    {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }
}
