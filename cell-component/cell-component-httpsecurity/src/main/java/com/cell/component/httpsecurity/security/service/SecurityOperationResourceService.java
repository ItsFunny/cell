package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityOperationResource;
import com.cell.component.httpsecurity.security.mapper.SecurityOperationResourceMapper;

import java.util.List;


public interface SecurityOperationResourceService extends IService<SecurityOperationResource> {

    SecurityOperationResourceMapper getSecurityOperationResourceMapper();

    SecurityOperationResource getByEntity(SecurityOperationResource securityOperationResource);

    List<SecurityOperationResource> listByEntity(SecurityOperationResource securityOperationResource);


    int insert(SecurityOperationResource securityOperationResource);

    int insertBatch(List<SecurityOperationResource> list);

    int update(SecurityOperationResource securityOperationResource);

    int updateBatch(List<SecurityOperationResource> list);

    int deleteById(Integer opId);

    int deleteByEntity(SecurityOperationResource securityOperationResource);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityOperationResource securityOperationResource);
}

