package com.cell.component.httpsecurity.security.models;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 12:28 下午
 */
public class DefaultTokenInfo extends UsernamePasswordAuthenticationToken
{
    public static final String LOGIN_TYPE = "loginType";
    public static final String LOGIN_KEY = "loginKey";
    public static final String LOGIN_PASSWORD = "loginValue";

    public static final Integer USERNAME_PWD_LOGIN = 1 << 0;

    private int loginType;
    private String loginKey;
    private String loginPwd;


    public String getLoginKey()
    {
        return loginKey;
    }

    public void setLoginKey(String loginKey)
    {
        this.loginKey = loginKey;
    }

    public String getLoginPwd()
    {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd)
    {
        this.loginPwd = loginPwd;
    }

    public DefaultTokenInfo(Object principal, Object credentials)
    {
        super(principal, credentials);
    }

    public DefaultTokenInfo(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
    {
        super(principal, credentials, authorities);
    }


    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LOGIN_KEY, this.loginKey);
        jsonObject.put(LOGIN_PASSWORD, this.loginPwd);
        return jsonObject;
    }

    public int getLoginType()
    {
        return loginType;
    }

    public void setLoginType(int loginType)
    {
        this.loginType = loginType;
    }
}
