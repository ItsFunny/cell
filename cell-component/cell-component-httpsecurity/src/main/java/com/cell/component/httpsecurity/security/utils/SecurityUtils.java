package com.cell.component.httpsecurity.security.utils;

import com.cell.base.common.utils.JSONUtil;
import com.cell.component.httpsecurity.security.constants.SecurityConstants;
import com.cell.component.httpsecurity.security.entity.SecurityUserInfo;
import com.cell.component.httpsecurity.security.impl.SecurityUser;
import com.cell.component.httpsecurity.security.models.DefaultAuthenticationToken;
import com.cell.node.core.context.CellContext;
import com.cell.node.core.exception.BusinessException;
import com.cell.node.core.exception.ErrorConstant;
import com.cell.node.core.exception.WrapContextException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class SecurityUtils {
    public static String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }

    public static void checkAdminType(Integer adminType) {
        if (!adminType.equals(SecurityConstants.ADMIN_LEVEL_SUPER_ADMIN) && !adminType.equals(SecurityConstants.ADMIN_LEVEL_NORMAL_ADMIN)) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkUserAvailable(SecurityUserInfo securityUserInfo) {
        if (securityUserInfo == null) {
            throw new PreAuthenticatedCredentialsNotFoundException("User Not Exist");
        }
        if (SecurityConstants.DB_USER_STATUS_DISABLE.equals(securityUserInfo.getStatus())) {
            throw new BusinessException(ErrorConstant.USER_FORBIDDEN);
        }
    }

    public static SecurityUser getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRETKEY).parseClaimsJws(token).getBody();
            String userStr = (String) claims.get("sub");
            SecurityUser ret = JSONUtil.json2Obj(userStr, SecurityUser.class);
            if (ret.expired()) {
                throw new BusinessException(ErrorConstant.TOKEN_EXPIRED);
            }
            return ret;
        } catch (Exception e) {
            if (e.getMessage().contains("JWT expired") || e.getMessage().contains("TOKEN EXPIRED")) {
                throw e;
            }
            if (e.getMessage().contains("not match locally computed signature")) {
                throw new BusinessException(500, "keyNotMatch");
            }
            throw new BusinessException(ErrorConstant.TOKEN_ILLEGAL);
        }
    }

    public static SecurityUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        DefaultAuthenticationToken token = (DefaultAuthenticationToken) authentication;
        return (SecurityUser) token.getPrincipal();
    }

    public static SecurityUser getCurrentUser(CellContext ctx) {
        if (getUserFromContext(ctx) != null) {
            return getUserFromContext(ctx);
        }
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new WrapContextException(ctx, new BusinessException(ErrorConstant.USER_NOT_LOGIN));
        }
        DefaultAuthenticationToken token = (DefaultAuthenticationToken) authentication;
        SecurityUser user = (SecurityUser) token.getPrincipal();
        setUserToContext(ctx, user);
        return user;
    }

    public static SecurityUser getUserFromContext(CellContext context) {
        Object attribute = context.getAttribute(SecurityConstants.ContextUserKey);
        if (attribute == null) {
            return null;
        }
        return (SecurityUser) attribute;
    }

    public static void setUserToContext(CellContext context, SecurityUser securityUser) {
        context.setAttribute(SecurityConstants.ContextUserKey, securityUser);
    }

    public static String createSecurityToken(SecurityUser user) {
        String subject = JSONUtil.obj2Json(user);
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .claim("userId", user.getUserId())
                .claim("password", user.getPassword())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRETKEY);
        return builder.compact();
    }

    public static String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("authorization");
    }

    public static CellContext mustGetContext(HttpServletRequest request)
    {
        CellContext context = (CellContext) request.getAttribute(CellContext.CELL_CONTEXT);
        if (null == context)
        {
            throw new RuntimeException("framework error");
        }
        return context;
    }
}
