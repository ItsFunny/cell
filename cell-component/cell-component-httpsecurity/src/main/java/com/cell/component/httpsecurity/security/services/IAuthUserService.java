package com.cell.component.httpsecurity.security.services;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 4:44 下午
 */
public interface IAuthUserService
{
    boolean hashRole(String role);

    boolean hasPermission(String permissionRule);

    boolean hashAnyRole(Collection<String> role);

    boolean hashAnyOperationPermission(Collection<String> permission);

    boolean hashOperationPermission(String permission);
}
