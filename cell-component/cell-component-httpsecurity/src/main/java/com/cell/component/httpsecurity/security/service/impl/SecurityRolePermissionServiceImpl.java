package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityRolePermissionMapper;
import com.cell.component.httpsecurity.security.entity.SecurityRolePermission;
import com.cell.component.httpsecurity.security.service.SecurityRolePermissionService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityRolePermissionMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityRolePermissionService")
public class SecurityRolePermissionServiceImpl extends ServiceImpl<SecurityRolePermissionMapper, SecurityRolePermission> implements SecurityRolePermissionService {

    @Resource(type = SecurityRolePermissionMapper.class)
    private SecurityRolePermissionMapper securityRolePermissionMapper;

    @Override
    public SecurityRolePermissionMapper getSecurityRolePermissionMapper() {
        return securityRolePermissionMapper;
    }


    @Override
    public SecurityRolePermission getByEntity(SecurityRolePermission securityRolePermission) {
        return securityRolePermissionMapper.getByEntity(securityRolePermission);
    }

    @Override
    public List<SecurityRolePermission> listByEntity(SecurityRolePermission securityRolePermission) {
        return securityRolePermissionMapper.listByEntity(securityRolePermission);
    }


    @Override
    public int insert(SecurityRolePermission securityRolePermission) {
        return securityRolePermissionMapper.insert(securityRolePermission);
    }

    @Override
    public int insertBatch(List<SecurityRolePermission> list) {
        return securityRolePermissionMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityRolePermission securityRolePermission) {
        return securityRolePermissionMapper.update(securityRolePermission);
    }

    @Override
    public int updateBatch(List<SecurityRolePermission> list) {
        return securityRolePermissionMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer id) {
        return securityRolePermissionMapper.deleteById(id);
    }

    @Override
    public int deleteByEntity(SecurityRolePermission securityRolePermission) {
        return securityRolePermissionMapper.deleteByEntity(securityRolePermission);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityRolePermissionMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityRolePermissionMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityRolePermission securityRolePermission) {
        return securityRolePermissionMapper.countByEntity(securityRolePermission);
    }

}

