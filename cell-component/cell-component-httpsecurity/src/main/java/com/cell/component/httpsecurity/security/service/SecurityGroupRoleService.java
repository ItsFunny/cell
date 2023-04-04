package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityGroupRole;
import com.cell.component.httpsecurity.security.mapper.SecurityGroupRoleMapper;

import java.util.List;


public interface SecurityGroupRoleService extends IService<SecurityGroupRole> {

    SecurityGroupRoleMapper getSecurityGroupRoleMapper();

    SecurityGroupRole getByEntity(SecurityGroupRole securityGroupRole);

    List<SecurityGroupRole> listByEntity(SecurityGroupRole securityGroupRole);


    int insert(SecurityGroupRole securityGroupRole);

    int insertBatch(List<SecurityGroupRole> list);

    int update(SecurityGroupRole securityGroupRole);

    int updateBatch(List<SecurityGroupRole> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityGroupRole securityGroupRole);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityGroupRole securityGroupRole);
}

