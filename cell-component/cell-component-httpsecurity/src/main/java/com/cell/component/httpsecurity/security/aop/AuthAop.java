package com.cell.component.httpsecurity.security.aop;

import com.cell.base.common.utils.StringUtils;
import com.cell.component.httpsecurity.security.annotation.AuthAnnotation;
import com.cell.component.httpsecurity.security.constants.SecurityConstants;
import com.cell.component.httpsecurity.security.services.IAuthUserService;
import com.cell.node.core.context.CellContext;
import com.cell.node.core.exception.BusinessException;
import com.cell.node.core.exception.ErrorConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
public class AuthAop {
    private static boolean skipAuth = false;
    @Autowired
    private IAuthUserService authUserService;

    @Pointcut("@annotation(com.cell.component.httpsecurity.security.annotation.AuthAnnotation)")
    public void authAnno() {}

    @Before("authAnno()")
    public void beforeAuthCheck(JoinPoint joinPoint) {
        if (skipAuth) return;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (!(arg instanceof CellContext)) {
                continue;
            }
            CellContext context = (CellContext) arg;
            if (!context.skipAuth()) {
                MethodSignature sign = (MethodSignature) joinPoint.getSignature();
                Method method = sign.getMethod();
                AuthAnnotation annotation = method.getAnnotation(AuthAnnotation.class);
                String[] roles = annotation.allowRole();
                this.checkRoles(roles, annotation);
                String[] permissions = annotation.allowPermissions();
                this.checkPermissions(permissions, annotation);
            }
            return;
        }
    }

    private void checkRoles(String[] roleStrs, AuthAnnotation annotation) {
        Set<String> roles = this.filterStr(roleStrs);
        if (roles.isEmpty()) return;
        if (annotation.RoleType() == SecurityConstants.ANY) {
            if (!authUserService.hashAnyRole(roles)) {
                throw new BusinessException(ErrorConstant.AUTH_FAILED);
            }
        }
    }

    private void checkPermissions(String[] permissionStrs, AuthAnnotation annotation) {
        Set<String> permissions = this.filterStr(permissionStrs);
        if (permissions.isEmpty()) return;
        if (annotation.PermissionType() == SecurityConstants.ANY) {
            if (!authUserService.hashAnyOperationPermission(permissions)) {
                throw new BusinessException(ErrorConstant.AUTH_FAILED);
            }
        }
    }

    private Set<String> filterStr(String... str) {
        return Stream.of(str).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
    }

    public static void setSkipAuth(boolean b) {
        skipAuth = b;
    }
}
