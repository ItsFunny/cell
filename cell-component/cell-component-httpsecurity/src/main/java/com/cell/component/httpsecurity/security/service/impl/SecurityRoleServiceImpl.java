package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityRoleMapper;
import com.cell.component.httpsecurity.security.entity.SecurityRole;
import com.cell.component.httpsecurity.security.service.SecurityRoleService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityRoleMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityRoleService")
public class SecurityRoleServiceImpl extends ServiceImpl<SecurityRoleMapper, SecurityRole> implements SecurityRoleService {

    @Resource(type = SecurityRoleMapper.class)
    private SecurityRoleMapper securityRoleMapper;

    @Override
    public SecurityRoleMapper getSecurityRoleMapper() {
        return securityRoleMapper;
    }


    @Override
    public SecurityRole getByEntity(SecurityRole securityRole) {
        return securityRoleMapper.getByEntity(securityRole);
    }

    @Override
    public List<SecurityRole> listByEntity(SecurityRole securityRole) {
        return securityRoleMapper.listByEntity(securityRole);
    }


    @Override
    public int insert(SecurityRole securityRole) {
        return securityRoleMapper.insert(securityRole);
    }

    @Override
    public int insertBatch(List<SecurityRole> list) {
        return securityRoleMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityRole securityRole) {
        return securityRoleMapper.update(securityRole);
    }

    @Override
    public int updateBatch(List<SecurityRole> list) {
        return securityRoleMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer roleId) {
        return securityRoleMapper.deleteById(roleId);
    }

    @Override
    public int deleteByEntity(SecurityRole securityRole) {
        return securityRoleMapper.deleteByEntity(securityRole);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityRoleMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityRoleMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityRole securityRole) {
        return securityRoleMapper.countByEntity(securityRole);
    }

}

