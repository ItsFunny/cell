package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityPermissionMapper;
import com.cell.component.httpsecurity.security.entity.SecurityPermission;
import com.cell.component.httpsecurity.security.service.SecurityPermissionService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityPermissionMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityPermissionService")
public class SecurityPermissionServiceImpl extends ServiceImpl<SecurityPermissionMapper, SecurityPermission> implements SecurityPermissionService {

    @Resource(type = SecurityPermissionMapper.class)
    private SecurityPermissionMapper securityPermissionMapper;

    @Override
    public SecurityPermissionMapper getSecurityPermissionMapper() {
        return securityPermissionMapper;
    }


    @Override
    public SecurityPermission getByEntity(SecurityPermission securityPermission) {
        return securityPermissionMapper.getByEntity(securityPermission);
    }

    @Override
    public List<SecurityPermission> listByEntity(SecurityPermission securityPermission) {
        return securityPermissionMapper.listByEntity(securityPermission);
    }


    @Override
    public int insert(SecurityPermission securityPermission) {
        return securityPermissionMapper.insert(securityPermission);
    }

    @Override
    public int insertBatch(List<SecurityPermission> list) {
        return securityPermissionMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityPermission securityPermission) {
        return securityPermissionMapper.update(securityPermission);
    }

    @Override
    public int updateBatch(List<SecurityPermission> list) {
        return securityPermissionMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer permissionId) {
        return securityPermissionMapper.deleteById(permissionId);
    }

    @Override
    public int deleteByEntity(SecurityPermission securityPermission) {
        return securityPermissionMapper.deleteByEntity(securityPermission);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityPermissionMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityPermissionMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityPermission securityPermission) {
        return securityPermissionMapper.countByEntity(securityPermission);
    }

}

