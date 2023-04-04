package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityGroup;
import com.cell.component.httpsecurity.security.mapper.SecurityGroupMapper;

import java.util.List;


public interface SecurityGroupService extends IService<SecurityGroup> {

    SecurityGroupMapper getSecurityGroupMapper();

    SecurityGroup getByEntity(SecurityGroup securityGroup);

    List<SecurityGroup> listByEntity(SecurityGroup securityGroup);


    int insert(SecurityGroup securityGroup);

    int insertBatch(List<SecurityGroup> list);

    int update(SecurityGroup securityGroup);

    int updateBatch(List<SecurityGroup> list);

    int deleteById(Integer groupId);

    int deleteByEntity(SecurityGroup securityGroup);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityGroup securityGroup);
}

