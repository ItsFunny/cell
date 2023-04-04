package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityMenuResourceMapper;
import com.cell.component.httpsecurity.security.entity.SecurityMenuResource;
import com.cell.component.httpsecurity.security.service.SecurityMenuResourceService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityMenuResourceMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityMenuResourceService")
public class SecurityMenuResourceServiceImpl extends ServiceImpl<SecurityMenuResourceMapper, SecurityMenuResource> implements SecurityMenuResourceService {

    @Resource(type = SecurityMenuResourceMapper.class)
    private SecurityMenuResourceMapper securityMenuResourceMapper;

    @Override
    public SecurityMenuResourceMapper getSecurityMenuResourceMapper() {
        return securityMenuResourceMapper;
    }


    @Override
    public SecurityMenuResource getByEntity(SecurityMenuResource securityMenuResource) {
        return securityMenuResourceMapper.getByEntity(securityMenuResource);
    }

    @Override
    public List<SecurityMenuResource> listByEntity(SecurityMenuResource securityMenuResource) {
        return securityMenuResourceMapper.listByEntity(securityMenuResource);
    }


    @Override
    public int insert(SecurityMenuResource securityMenuResource) {
        return securityMenuResourceMapper.insert(securityMenuResource);
    }

    @Override
    public int insertBatch(List<SecurityMenuResource> list) {
        return securityMenuResourceMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityMenuResource securityMenuResource) {
        return securityMenuResourceMapper.update(securityMenuResource);
    }

    @Override
    public int updateBatch(List<SecurityMenuResource> list) {
        return securityMenuResourceMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer menuResourceId) {
        return securityMenuResourceMapper.deleteById(menuResourceId);
    }

    @Override
    public int deleteByEntity(SecurityMenuResource securityMenuResource) {
        return securityMenuResourceMapper.deleteByEntity(securityMenuResource);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityMenuResourceMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityMenuResourceMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityMenuResource securityMenuResource) {
        return securityMenuResourceMapper.countByEntity(securityMenuResource);
    }

}

