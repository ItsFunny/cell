package com.cell.component.httpsecurity.security.filter;

import com.cell.component.httpsecurity.security.entity.SecurityUserInfo;
import com.cell.component.httpsecurity.security.impl.SecurityUser;
import com.cell.component.httpsecurity.security.models.AuthenticationFactory;
import com.cell.component.httpsecurity.security.service.SecurityUserInfoService;
import com.cell.component.httpsecurity.security.utils.SecurityUtils;
import com.cell.node.core.context.CellContext;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.ContextUtils;
import com.mi.wallet.mange.entity.SecurityUserInfo;
import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.models.AuthenticationFactory;
import com.mi.wallet.mange.security.utils.SecurityUtils;
import com.mi.wallet.mange.service.SecurityUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationFilter implements Filter {

    private RequestMatcher requestMatcher;

    private SecurityUserInfoService userInfoService;

    public TokenAuthenticationFilter(SecurityUserInfoService userInfoService) {
        this.requestMatcher = new AntPathRequestMatcher("/api/v1/**");
        this.userInfoService = userInfoService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!this.requestMatcher.matches((HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            final String authHeader = SecurityUtils.getTokenFromHeader((HttpServletRequest) request);
            if (!StringUtils.isEmpty(authHeader)) {
                SecurityUser userFromToken = SecurityUtils.getUserFromToken(authHeader);
                authentication = AuthenticationFactory.newDefaultAuthentication(userFromToken);
                SecurityUserInfo info = this.userInfoService.getById(userFromToken.getUserId());
                SecurityUtils.checkUserAvailable(info);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new PreAuthenticatedCredentialsNotFoundException("用户登录信息不存在");
            }
        }
        CellContext ctx = SecurityUtils.mustGetContext((HttpServletRequest) request);
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        ctx.setCurrentUser(user);
        chain.doFilter(request, response);
    }
}
