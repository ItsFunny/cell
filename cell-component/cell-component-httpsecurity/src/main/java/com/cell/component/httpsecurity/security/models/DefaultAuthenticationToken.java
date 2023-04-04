package com.cell.component.httpsecurity.security.models;

import com.cell.component.httpsecurity.security.impl.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 12:39 下午
 */
public class DefaultAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public DefaultAuthenticationToken(SecurityUser principal) {
        super(principal, null, null);
    }
}
