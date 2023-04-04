package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityUserGroup;
import com.cell.component.httpsecurity.security.mapper.SecurityUserGroupMapper;

import java.util.List;


public interface SecurityUserGroupService extends IService<SecurityUserGroup> {

    SecurityUserGroupMapper getSecurityUserGroupMapper();

    SecurityUserGroup getByEntity(SecurityUserGroup securityUserGroup);

    List<SecurityUserGroup> listByEntity(SecurityUserGroup securityUserGroup);


    int insert(SecurityUserGroup securityUserGroup);

    int insertBatch(List<SecurityUserGroup> list);

    int update(SecurityUserGroup securityUserGroup);

    int updateBatch(List<SecurityUserGroup> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityUserGroup securityUserGroup);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityUserGroup securityUserGroup);
}

