package com.cell.component.httpsecurity.security.impl;

import com.mi.wallet.mange.security.IUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 2:01 下午
 */
public abstract class AbstractUserDetailService implements BaseTemplate<Integer>, IUserDetailService
{
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        throw new RuntimeException("not supported");
    }
}
