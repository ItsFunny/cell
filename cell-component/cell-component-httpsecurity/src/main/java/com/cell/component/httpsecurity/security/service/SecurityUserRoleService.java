package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityUserRole;
import com.cell.component.httpsecurity.security.mapper.SecurityUserRoleMapper;

import java.util.List;


public interface SecurityUserRoleService extends IService<SecurityUserRole> {

    SecurityUserRoleMapper getSecurityUserRoleMapper();

    SecurityUserRole getByEntity(SecurityUserRole securityUserRole);

    List<SecurityUserRole> listByEntity(SecurityUserRole securityUserRole);


    int insert(SecurityUserRole securityUserRole);

    int insertBatch(List<SecurityUserRole> list);

    int update(SecurityUserRole securityUserRole);

    int updateBatch(List<SecurityUserRole> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityUserRole securityUserRole);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityUserRole securityUserRole);
}

