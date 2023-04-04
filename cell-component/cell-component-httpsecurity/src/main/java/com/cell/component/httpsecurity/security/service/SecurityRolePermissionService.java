package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityRolePermission;
import com.cell.component.httpsecurity.security.mapper.SecurityRolePermissionMapper;

import java.util.List;


public interface SecurityRolePermissionService extends IService<SecurityRolePermission> {

    SecurityRolePermissionMapper getSecurityRolePermissionMapper();

    SecurityRolePermission getByEntity(SecurityRolePermission securityRolePermission);

    List<SecurityRolePermission> listByEntity(SecurityRolePermission securityRolePermission);


    int insert(SecurityRolePermission securityRolePermission);

    int insertBatch(List<SecurityRolePermission> list);

    int update(SecurityRolePermission securityRolePermission);

    int updateBatch(List<SecurityRolePermission> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityRolePermission securityRolePermission);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityRolePermission securityRolePermission);
}

