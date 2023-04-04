package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityPermission;
import com.cell.component.httpsecurity.security.mapper.SecurityPermissionMapper;

import java.util.List;


public interface SecurityPermissionService extends IService<SecurityPermission> {

    SecurityPermissionMapper getSecurityPermissionMapper();

    SecurityPermission getByEntity(SecurityPermission securityPermission);

    List<SecurityPermission> listByEntity(SecurityPermission securityPermission);


    int insert(SecurityPermission securityPermission);

    int insertBatch(List<SecurityPermission> list);

    int update(SecurityPermission securityPermission);

    int updateBatch(List<SecurityPermission> list);

    int deleteById(Integer permissionId);

    int deleteByEntity(SecurityPermission securityPermission);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityPermission securityPermission);
}

