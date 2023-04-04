package com.cell.component.httpsecurity.security;

import com.mi.wallet.mange.context.IExceptionHandler;
import com.mi.wallet.mange.security.config.DefaultSecurityConfigurer;
import com.mi.wallet.mange.security.engine.DefaultSimpleAuthRuleEngine;
import com.mi.wallet.mange.security.filter.DefaultSecurityExceptionFilter;
import com.mi.wallet.mange.security.filter.IAuthRuleEngine;
import com.mi.wallet.mange.security.funcs.DefaultRequestReceived;
import com.mi.wallet.mange.security.funs.OnRequestReceived;
import com.mi.wallet.mange.security.handler.*;
import com.mi.wallet.mange.security.impl.DefaultAuthenticationManager;
import com.mi.wallet.mange.security.impl.UserDetailServiceProxy;
import com.mi.wallet.mange.security.services.IAuthUserService;
import com.mi.wallet.mange.security.services.impl.DefaultAuthServiceImpl;
import com.mi.wallet.mange.service.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 1:57 下午
 */
@Configuration
public class SecurityConfiguration implements InitializingBean {
    // login
    @Bean
    public IUserDetailService defaultUserName(SecurityUserInfoService azerUserService,
                                              SecurityUserRoleService userRoleService,
                                              SecurityGroupRoleService azerGroupRoleService,
                                              SecurityPermissionService permissionService,
                                              SecurityMenuResourceService menuResourceService,
                                              SecurityOperationResourceService operationResourceService,
                                              SecurityRolePermissionService rolePermissionService,
//                                              AzerPermissionMenuResourceService permissionMenuResourceService,
                                              SecurityRoleService roleService
    ) {
        DefaultUserNamePasswordServiceImpl ret = new DefaultUserNamePasswordServiceImpl(
                azerUserService,
                userRoleService,
                azerGroupRoleService,
                permissionService, menuResourceService, operationResourceService,
                rolePermissionService,
                roleService
        );
        return ret;
    }

    @Bean
    public UserDetailServiceProxy userDetailService(List<IUserDetailService> userDetailServices) {
        return new UserDetailServiceProxy(userDetailServices);
    }

    @Bean
    public AuthenticationProvider provider(UserDetailServiceProxy proxy) {
        return new DefaultAuthenticationProvider(proxy);
    }


    @Bean
    public DefaultSecurityExceptionFilter exceptionFilter(IExceptionHandler exceptionHandler) {
        DefaultSecurityExceptionFilter ret = new DefaultSecurityExceptionFilter(exceptionHandler);
        return ret;
    }

    @Bean
    public AuthSuccessHandler authSuccessHandler() {
        DefaultAuthSuccessHandler authSuccessHandler = new DefaultAuthSuccessHandler();
        return authSuccessHandler;
    }

    @Bean
    public AuthFailHandler authFailHandler(IExceptionHandler exceptionHandler) {
        DefaultAuthFailHandler authFailHandler = new DefaultAuthFailHandler(exceptionHandler);
        return authFailHandler;
    }

    @Bean
    public LoginSuccessHandler successHandler() {
        DefaultLoginSuccessHandler ret = new DefaultLoginSuccessHandler();
        return ret;
    }

    @Bean
    public LoginFailHandler failureHandler(IExceptionHandler handler) {
        DefaultLoginFailureHandler ret = new DefaultLoginFailureHandler(handler);
        return ret;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider provider) {
        return new DefaultAuthenticationManager(provider);
    }

    @Bean
    public OnRequestReceived requestReceived() {
        return new DefaultRequestReceived();
    }

    @Bean
    public IAuthUserService authUserService() {
        return new DefaultAuthServiceImpl();
    }

    @Bean
    public IAuthRuleEngine ruleEngine(IAuthUserService userService) {
        return new DefaultSimpleAuthRuleEngine(userService);
    }


    @Bean
    public DefaultSecurityConfigurer securityConfigurer(
            AuthenticationManager authenticationManager,
            DefaultSecurityExceptionFilter exceptionFilter,
            LoginFailHandler failureHandler,
            LoginSuccessHandler successHandler,
            AuthFailHandler authFailHandler,
            AuthSuccessHandler authSuccessHandler,
            OnRequestReceived requestReceived,
            IAuthRuleEngine ruleEngine,
            SecurityUserInfoService userInfoService
    ) {
        return new DefaultSecurityConfigurer(authenticationManager,
                exceptionFilter,
                failureHandler,
                successHandler,
                authFailHandler,
                authSuccessHandler,
                requestReceived,
                ruleEngine,userInfoService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserLRUCache instance = UserLRUCache.getInstance();
        instance.seal();
    }
}
