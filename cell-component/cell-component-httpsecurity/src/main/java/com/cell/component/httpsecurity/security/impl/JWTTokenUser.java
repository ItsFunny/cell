package com.cell.component.httpsecurity.security.impl;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class JWTTokenUser
{
    private Long id;
    // 用户拥有的角色
    private Set<String> roles = new HashSet<>(1);
    // 用户拥有的所有数据权限
    private Set<String> dataPermissionSet = new HashSet<>(1);
    // 用户拥有的所有操作权限
    private Set<String> operationPermissionSet = new HashSet<>(1);
}
