package com.cell.component.httpsecurity.security.impl;

import com.alibaba.fastjson.JSONObject;
import com.mi.wallet.mange.security.IUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 2:06 下午
 */
public class UserDetailServiceProxy implements IUserDetailService
{
    private List<IUserDetailService> userServices;

    public UserDetailServiceProxy(List<IUserDetailService> userServices)
    {
        this.userServices = userServices;
    }

    @Override
    public UserDetails loadUserByUsername(JSONObject jsonObject, Integer loginType) throws UsernameNotFoundException
    {
        for (IUserDetailService userDetailService:this.userServices){
            if (!userDetailService.validIsMine(loginType)){
                continue;
            }
            UserDetails userDetails = userDetailService.loadUserByUsername(jsonObject, loginType);
            if (userDetails!=null){
                return userDetails;
            }
        }
        throw new UsernameNotFoundException("asd");
//        Optional<UserDetails> first = this.userServices.stream()
//                .filter(p -> p.validIsMine(loginType))
//                .map(u -> u.loadUserByUsername(jsonObject, loginType)).findFirst();
//        if (first.isPresent())
//        {
//            return first.get();
//        }
//        throw new UsernameNotFoundException("asd");
    }

    @Override
    public boolean validIsMine(Integer type)
    {
        throw new RuntimeException("panic");
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        throw new RuntimeException("panic");
    }

}