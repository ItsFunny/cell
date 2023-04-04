package com.cell.component.httpsecurity.security;

import com.alibaba.fastjson.JSONObject;
import com.mi.wallet.mange.security.impl.BaseTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 12:27 下午
 */
public interface IUserDetailService extends UserDetailsService , BaseTemplate<Integer>
{
    UserDetails loadUserByUsername(JSONObject jsonObject, Integer loginType) throws UsernameNotFoundException;
}
