package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityGroupRoleMapper;
import com.cell.component.httpsecurity.security.entity.SecurityGroupRole;
import com.cell.component.httpsecurity.security.service.SecurityGroupRoleService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityGroupRoleMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityGroupRoleService")
public class SecurityGroupRoleServiceImpl extends ServiceImpl<SecurityGroupRoleMapper, SecurityGroupRole> implements SecurityGroupRoleService {

    @Resource(type = SecurityGroupRoleMapper.class)
    private SecurityGroupRoleMapper securityGroupRoleMapper;

    @Override
    public SecurityGroupRoleMapper getSecurityGroupRoleMapper() {
        return securityGroupRoleMapper;
    }


    @Override
    public SecurityGroupRole getByEntity(SecurityGroupRole securityGroupRole) {
        return securityGroupRoleMapper.getByEntity(securityGroupRole);
    }

    @Override
    public List<SecurityGroupRole> listByEntity(SecurityGroupRole securityGroupRole) {
        return securityGroupRoleMapper.listByEntity(securityGroupRole);
    }


    @Override
    public int insert(SecurityGroupRole securityGroupRole) {
        return securityGroupRoleMapper.insert(securityGroupRole);
    }

    @Override
    public int insertBatch(List<SecurityGroupRole> list) {
        return securityGroupRoleMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityGroupRole securityGroupRole) {
        return securityGroupRoleMapper.update(securityGroupRole);
    }

    @Override
    public int updateBatch(List<SecurityGroupRole> list) {
        return securityGroupRoleMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer id) {
        return securityGroupRoleMapper.deleteById(id);
    }

    @Override
    public int deleteByEntity(SecurityGroupRole securityGroupRole) {
        return securityGroupRoleMapper.deleteByEntity(securityGroupRole);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityGroupRoleMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityGroupRoleMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityGroupRole securityGroupRole) {
        return securityGroupRoleMapper.countByEntity(securityGroupRole);
    }

}

