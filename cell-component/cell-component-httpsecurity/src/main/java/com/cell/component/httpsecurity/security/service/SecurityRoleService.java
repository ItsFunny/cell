package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityRole;
import com.cell.component.httpsecurity.security.mapper.SecurityRoleMapper;

import java.util.List;


public interface SecurityRoleService extends IService<SecurityRole> {

    SecurityRoleMapper getSecurityRoleMapper();

    SecurityRole getByEntity(SecurityRole securityRole);

    List<SecurityRole> listByEntity(SecurityRole securityRole);


    int insert(SecurityRole securityRole);

    int insertBatch(List<SecurityRole> list);

    int update(SecurityRole securityRole);

    int updateBatch(List<SecurityRole> list);

    int deleteById(Integer roleId);

    int deleteByEntity(SecurityRole securityRole);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityRole securityRole);
}

