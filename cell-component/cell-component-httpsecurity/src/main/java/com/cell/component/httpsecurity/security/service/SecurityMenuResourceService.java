package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityMenuResource;
import com.cell.component.httpsecurity.security.mapper.SecurityMenuResourceMapper;

import java.util.List;


public interface SecurityMenuResourceService extends IService<SecurityMenuResource> {

    SecurityMenuResourceMapper getSecurityMenuResourceMapper();

    SecurityMenuResource getByEntity(SecurityMenuResource securityMenuResource);

    List<SecurityMenuResource> listByEntity(SecurityMenuResource securityMenuResource);


    int insert(SecurityMenuResource securityMenuResource);

    int insertBatch(List<SecurityMenuResource> list);

    int update(SecurityMenuResource securityMenuResource);

    int updateBatch(List<SecurityMenuResource> list);

    int deleteById(Integer menuResourceId);

    int deleteByEntity(SecurityMenuResource securityMenuResource);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityMenuResource securityMenuResource);
}

