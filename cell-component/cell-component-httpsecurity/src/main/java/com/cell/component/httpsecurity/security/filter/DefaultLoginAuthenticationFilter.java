package com.cell.component.httpsecurity.security.filter;

import com.cell.base.common.utils.StringUtils;
import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.ContextUtils;
import com.mi.wallet.mange.context.ModuleEnums;
import com.mi.wallet.mange.security.funs.OnRequestReceived;
import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 1:38 下午
 */
public class DefaultLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    public static final Integer USERNAME_PWD_LOGIN = 1 << 0;

    private OnRequestReceived requestReceived;


    public DefaultLoginAuthenticationFilter(AuthenticationManager authenticationManager,
                                            AuthenticationSuccessHandler successHandler,
                                            AuthenticationFailureHandler failureHandler,
                                            OnRequestReceived requestReceived,
                                            String loginProcessUrl)
    {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(loginProcessUrl));
        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
        this.setAuthenticationManager(authenticationManager);
        this.requestReceived = requestReceived;
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response)
    {
        String requestPath = SecurityUtils.getRequestPath(request);
        // TODO
        if (StringUtils.isNotEmpty(requestPath)&&requestPath.contains("logout")){
            return false;
        }
        return super.requiresAuthentication(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        Authentication authentication = null;
        Authentication authRequest = this.requestReceived.onRecv(request);
        LOG.info(ModuleEnums.WEB_SECURITY, "生成的authRequest为:" + authRequest);

        authentication = this.getAuthenticationManager().authenticate(authRequest);
        // TODO ,npe
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        CellContext context = ContextUtils.mustGetContext(request);
        context.setCurrentUser((SecurityUser) authentication.getPrincipal());
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        return authentication;
    }
}
