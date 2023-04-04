package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityOperationResourceMapper;
import com.cell.component.httpsecurity.security.entity.SecurityOperationResource;
import com.cell.component.httpsecurity.security.service.SecurityOperationResourceService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityOperationResourceMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityOperationResourceService")
public class SecurityOperationResourceServiceImpl extends ServiceImpl<SecurityOperationResourceMapper, SecurityOperationResource> implements SecurityOperationResourceService {

    @Resource(type = SecurityOperationResourceMapper.class)
    private SecurityOperationResourceMapper securityOperationResourceMapper;

    @Override
    public SecurityOperationResourceMapper getSecurityOperationResourceMapper() {
        return securityOperationResourceMapper;
    }


    @Override
    public SecurityOperationResource getByEntity(SecurityOperationResource securityOperationResource) {
        return securityOperationResourceMapper.getByEntity(securityOperationResource);
    }

    @Override
    public List<SecurityOperationResource> listByEntity(SecurityOperationResource securityOperationResource) {
        return securityOperationResourceMapper.listByEntity(securityOperationResource);
    }


    @Override
    public int insert(SecurityOperationResource securityOperationResource) {
        return securityOperationResourceMapper.insert(securityOperationResource);
    }

    @Override
    public int insertBatch(List<SecurityOperationResource> list) {
        return securityOperationResourceMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityOperationResource securityOperationResource) {
        return securityOperationResourceMapper.update(securityOperationResource);
    }

    @Override
    public int updateBatch(List<SecurityOperationResource> list) {
        return securityOperationResourceMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer opId) {
        return securityOperationResourceMapper.deleteById(opId);
    }

    @Override
    public int deleteByEntity(SecurityOperationResource securityOperationResource) {
        return securityOperationResourceMapper.deleteByEntity(securityOperationResource);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityOperationResourceMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityOperationResourceMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityOperationResource securityOperationResource) {
        return securityOperationResourceMapper.countByEntity(securityOperationResource);
    }

}

