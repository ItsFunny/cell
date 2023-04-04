package com.cell.component.httpsecurity.security.impl;

import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.context.ModuleEnums;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 12:47 下午
 */
public class DefaultAuthenticationManager implements AuthenticationManager
{
    private AuthenticationProvider authenticationProvider;

    public DefaultAuthenticationManager(AuthenticationProvider authenticationProvider)
    {
        this.authenticationProvider = authenticationProvider;
    }
    private DefaultAuthenticationManager(){}

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        LOG.info(ModuleEnums.WEB_SECURITY, "收到请求,request:{}", authentication);
        return this.authenticationProvider.authenticate(authentication);
    }
}
