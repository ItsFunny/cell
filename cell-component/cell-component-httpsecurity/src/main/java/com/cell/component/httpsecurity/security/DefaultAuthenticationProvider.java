package com.cell.component.httpsecurity.security;

import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.models.DefaultAuthenticationToken;
import com.mi.wallet.mange.security.models.DefaultTokenInfo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 12:26 下午
 */
public class DefaultAuthenticationProvider implements AuthenticationProvider
{
    public DefaultAuthenticationProvider(IUserDetailService userDetailService)
    {
        this.userDetailService = userDetailService;
    }

    private IUserDetailService userDetailService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        DefaultTokenInfo tokenInfo = (DefaultTokenInfo) authentication;
        UserDetails userDetails = null;
        try
        {
            userDetails = this.userDetailService.loadUserByUsername(tokenInfo.toJsonObject(), tokenInfo.getLoginType());
        } catch (UsernameNotFoundException e)
        {
            throw new AuthenticationServiceException("获取用户失败", e);
        } catch (Exception e)
        {
            throw new AuthenticationServiceException("获取用户失败", e);
        }
        if (userDetails == null)
        {
            return null;
        }
        SecurityUser user = (SecurityUser) userDetails;
        DefaultAuthenticationToken ret = new DefaultAuthenticationToken(user);
        return ret;
    }

    @Override
    public boolean supports(Class<?> aClass)
    {
        return aClass.getName().equals(DefaultTokenInfo.class.getName());
    }
}
