package com.cell.component.httpsecurity.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.component.httpsecurity.security.entity.SecurityUserInfo;
import com.cell.component.httpsecurity.security.mapper.SecurityUserInfoMapper;

import java.util.List;


public interface SecurityUserInfoService extends IService<SecurityUserInfo> {

    SecurityUserInfoMapper getSecurityUserInfoMapper();

    SecurityUserInfo getByEntity(SecurityUserInfo securityUserInfo);

    List<SecurityUserInfo> listByEntity(SecurityUserInfo securityUserInfo);


    int insert(SecurityUserInfo securityUserInfo);

    int insertBatch(List<SecurityUserInfo> list);

    int update(SecurityUserInfo securityUserInfo);

    int updateBatch(List<SecurityUserInfo> list);

    int deleteById(Integer userId);

    int deleteByEntity(SecurityUserInfo securityUserInfo);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityUserInfo securityUserInfo);
}

