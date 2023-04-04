package com.cell.component.httpsecurity.security.services.impl;

import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.impl.UserAuthority;
import com.mi.wallet.mange.security.services.IAuthUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Set;

public class DefaultAuthServiceImpl implements IAuthUserService {
    @Override
    public boolean hashRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if (user.isSuperAdmin()) {
            return true;
        }
        return user.getUserAuthority().getRoles().contains(role);
    }

    @Override
    public boolean hasPermission(String permissionRule) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if (user.isSuperAdmin()) {
            return true;
        }
        return user.getUserAuthority().getOperationPermissionSet().contains(permissionRule);
    }

    @Override
    public boolean hashAnyRole(Collection<String> role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if (user.isSuperAdmin()) {
            return true;
        }
        UserAuthority userAuthority = user.getUserAuthority();
        Set<String> roles = userAuthority.getRoles();
        return role.stream().anyMatch(p ->
                roles.contains(p));
    }

    @Override
    public boolean hashAnyOperationPermission(Collection<String> permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if (user.isSuperAdmin()) {
            return true;
        }
        UserAuthority userAuthority = user.getUserAuthority();
        Set<String> set = userAuthority.getOperationPermissionSet();
        return permission.stream().anyMatch(set::contains);
    }


    @Override
    public boolean hashOperationPermission(String permissionStr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        if (user.isSuperAdmin()) {
            return true;
        }
        UserAuthority userAuthority = user.getUserAuthority();
        Set<String> set = userAuthority.getOperationPermissionSet();
        return set.contains(permissionStr);
    }
}
