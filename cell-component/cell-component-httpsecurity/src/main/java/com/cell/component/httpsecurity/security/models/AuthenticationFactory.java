package com.cell.component.httpsecurity.security.models;


import com.cell.component.httpsecurity.security.impl.SecurityUser;

public class AuthenticationFactory
{
    public static DefaultAuthenticationToken newDefaultAuthentication(SecurityUser user){
        return new DefaultAuthenticationToken(user);
    }

}
