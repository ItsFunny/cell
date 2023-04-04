package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityUserRoleMapper;
import com.cell.component.httpsecurity.security.entity.SecurityUserRole;
import com.cell.component.httpsecurity.security.service.SecurityUserRoleService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityUserRoleMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityUserRoleService")
public class SecurityUserRoleServiceImpl extends ServiceImpl<SecurityUserRoleMapper, SecurityUserRole> implements SecurityUserRoleService {

    @Resource(type = SecurityUserRoleMapper.class)
    private SecurityUserRoleMapper securityUserRoleMapper;

    @Override
    public SecurityUserRoleMapper getSecurityUserRoleMapper() {
        return securityUserRoleMapper;
    }


    @Override
    public SecurityUserRole getByEntity(SecurityUserRole securityUserRole) {
        return securityUserRoleMapper.getByEntity(securityUserRole);
    }

    @Override
    public List<SecurityUserRole> listByEntity(SecurityUserRole securityUserRole) {
        return securityUserRoleMapper.listByEntity(securityUserRole);
    }


    @Override
    public int insert(SecurityUserRole securityUserRole) {
        return securityUserRoleMapper.insert(securityUserRole);
    }

    @Override
    public int insertBatch(List<SecurityUserRole> list) {
        return securityUserRoleMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityUserRole securityUserRole) {
        return securityUserRoleMapper.update(securityUserRole);
    }

    @Override
    public int updateBatch(List<SecurityUserRole> list) {
        return securityUserRoleMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer id) {
        return securityUserRoleMapper.deleteById(id);
    }

    @Override
    public int deleteByEntity(SecurityUserRole securityUserRole) {
        return securityUserRoleMapper.deleteByEntity(securityUserRole);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityUserRoleMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityUserRoleMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityUserRole securityUserRole) {
        return securityUserRoleMapper.countByEntity(securityUserRole);
    }

}

