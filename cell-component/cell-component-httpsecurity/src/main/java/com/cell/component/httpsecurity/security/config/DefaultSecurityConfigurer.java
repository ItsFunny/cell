package com.cell.component.httpsecurity.security.config;

import com.cell.base.common.context.InitCTX;
import com.cell.component.httpsecurity.security.filter.*;
import com.cell.component.httpsecurity.security.funs.OnRequestReceived;
import com.cell.component.httpsecurity.security.handler.AuthFailHandler;
import com.cell.component.httpsecurity.security.handler.AuthSuccessHandler;
import com.cell.component.httpsecurity.security.handler.LoginFailHandler;
import com.cell.component.httpsecurity.security.handler.LoginSuccessHandler;
import com.cell.component.httpsecurity.security.service.SecurityUserInfoService;
import com.mi.wallet.mange.security.ant.WhiteListAntPathMatcher;
import com.mi.wallet.mange.security.filter.*;
import com.mi.wallet.mange.security.funs.OnRequestReceived;
import com.mi.wallet.mange.security.handler.AuthFailHandler;
import com.mi.wallet.mange.security.handler.AuthSuccessHandler;
import com.mi.wallet.mange.security.handler.LoginFailHandler;
import com.mi.wallet.mange.security.handler.LoginSuccessHandler;
import com.mi.wallet.mange.service.SecurityUserInfoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashSet;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 12:46 下午
 */
public class DefaultSecurityConfigurer extends WebSecurityConfigurerAdapter implements InitializingBean {
    private AuthenticationManager authenticationManager;
    private DefaultSecurityExceptionFilter exceptionFilter;

    private LoginFailHandler loginFailHandler;
    private AuthFailHandler authFailHandler;

    private LoginSuccessHandler loginSuccessHandler;
    private AuthSuccessHandler authSuccessHandler;

    private OnRequestReceived requestReceived;
    private ContextPrepareFilter contextPrepareFilter;

    private AuthAuthenticationFilter authAuthenticationFilter;
    private IAuthRuleEngine engine;

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    public DefaultSecurityConfigurer(AuthenticationManager authenticationManager,
                                     DefaultSecurityExceptionFilter exceptionFilter,
                                     LoginFailHandler failureHandler,
                                     LoginSuccessHandler successHandler,
                                     AuthFailHandler authFailHandler,
                                     AuthSuccessHandler authSuccessHandler,
                                     OnRequestReceived requestReceived,
                                     IAuthRuleEngine ruleEngine,
                                     SecurityUserInfoService securityUserInfoService
    ) {
        this.authenticationManager = authenticationManager;
        this.exceptionFilter = exceptionFilter;
        this.loginFailHandler = failureHandler;
        this.loginSuccessHandler = successHandler;
        this.authFailHandler = authFailHandler;
        this.authSuccessHandler = authSuccessHandler;

        this.requestReceived = requestReceived;
        this.contextPrepareFilter = new ContextPrepareFilter();
        this.engine = ruleEngine;
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(securityUserInfoService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        SecurityConfigProperty property = SecurityConfigFactory.getInstance().getProperty();
        http.sessionManagement()
                .maximumSessions(1).expiredUrl(property.getLoginUrl());
        http.addFilterBefore(new DefaultLoginAuthenticationFilter(this.authenticationManager,
                this.loginSuccessHandler,
                this.loginFailHandler, this.requestReceived, property.getLoginUrl()), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(this.exceptionFilter, DefaultLoginAuthenticationFilter.class);
        http.addFilterBefore(this.contextPrepareFilter, DefaultSecurityExceptionFilter.class);
        http.addFilterAfter(this.authAuthenticationFilter, DefaultLoginAuthenticationFilter.class);
        http.addFilterBefore(this.tokenAuthenticationFilter, AuthAuthenticationFilter.class);

        http.cors().and().csrf().disable();

        http
                .authorizeRequests()
                .antMatchers(property.getWhiteUrlList().toArray(new String[property.getWhiteUrlList().size()]))
                .permitAll();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception
//    {
//        // allow Swagger URL to be accessed without authentication
//        web.ignoring().antMatchers(
//                "/swagger-ui.html",
//                "/v2/api-docs", // swagger api json
//                "/swagger-resources/configuration/ui", // 用来获取支持的动作
//                "/swagger-resources", // 用来获取api-docs的URI
//                "/swagger-resources/configuration/security", // 安全选项
//                "/swagger-resources/**",
//                //补充路径，近期在搭建swagger接口文档时，通过浏览器控制台发现该/webjars路径下的文件被拦截，故加上此过滤条件即可。(2020-10-23)
//                "/webjars/**"
//
//
//        );
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SecurityConfigFactory.getInstance().initOnce(new InitCTX());
        SecurityConfigProperty property = SecurityConfigFactory.getInstance().getProperty();
        RequestMatcher matcher = new WhiteListAntPathMatcher(new HashSet<>(property.getWhiteUrlList()), property.getAuthUrlPrefix());
        this.authAuthenticationFilter = new AuthAuthenticationFilter(this.engine, matcher, this.authSuccessHandler, this.authFailHandler);
    }
}
