package com.cell.component.httpsecurity.security.ant;


import com.cell.component.httpsecurity.security.utils.SecurityUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class WhiteListAntPathMatcher implements RequestMatcher {
    private Set<String> whiteUrlAddress;
    private AntPathRequestMatcher pathRequestMatcher;
    private static boolean authEnable = true;

    public static void setAuthEnable(boolean enable) {
        authEnable = enable;
    }

    public WhiteListAntPathMatcher(Set<String> whiteUrlAddress, String processUrl) {
        this.whiteUrlAddress = whiteUrlAddress;
        this.pathRequestMatcher = new AntPathRequestMatcher(processUrl);
    }

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        if (!authEnable) {
            return false;
        }
        if (this.whiteUrlAddress.contains(SecurityUtils.getRequestPath(httpServletRequest))) {
            return false;
        }
        return this.pathRequestMatcher.matches(httpServletRequest);
    }

}
