package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityGroupMapper;
import com.cell.component.httpsecurity.security.entity.SecurityGroup;
import com.cell.component.httpsecurity.security.service.SecurityGroupService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityGroupMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityGroupService")
public class SecurityGroupServiceImpl extends ServiceImpl<SecurityGroupMapper, SecurityGroup> implements SecurityGroupService {

    @Resource(type = SecurityGroupMapper.class)
    private SecurityGroupMapper securityGroupMapper;

    @Override
    public SecurityGroupMapper getSecurityGroupMapper() {
        return securityGroupMapper;
    }


    @Override
    public SecurityGroup getByEntity(SecurityGroup securityGroup) {
        return securityGroupMapper.getByEntity(securityGroup);
    }

    @Override
    public List<SecurityGroup> listByEntity(SecurityGroup securityGroup) {
        return securityGroupMapper.listByEntity(securityGroup);
    }


    @Override
    public int insert(SecurityGroup securityGroup) {
        return securityGroupMapper.insert(securityGroup);
    }

    @Override
    public int insertBatch(List<SecurityGroup> list) {
        return securityGroupMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityGroup securityGroup) {
        return securityGroupMapper.update(securityGroup);
    }

    @Override
    public int updateBatch(List<SecurityGroup> list) {
        return securityGroupMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer groupId) {
        return securityGroupMapper.deleteById(groupId);
    }

    @Override
    public int deleteByEntity(SecurityGroup securityGroup) {
        return securityGroupMapper.deleteByEntity(securityGroup);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityGroupMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityGroupMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityGroup securityGroup) {
        return securityGroupMapper.countByEntity(securityGroup);
    }

}

