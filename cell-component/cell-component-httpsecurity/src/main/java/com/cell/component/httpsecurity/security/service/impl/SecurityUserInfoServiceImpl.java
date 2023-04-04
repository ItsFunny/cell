package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityUserInfoMapper;
import com.cell.component.httpsecurity.security.entity.SecurityUserInfo;
import com.cell.component.httpsecurity.security.service.SecurityUserInfoService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityUserInfoMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityUserInfoService")
public class SecurityUserInfoServiceImpl extends ServiceImpl<SecurityUserInfoMapper, SecurityUserInfo> implements SecurityUserInfoService {

    @Resource(type = SecurityUserInfoMapper.class)
    private SecurityUserInfoMapper securityUserInfoMapper;

    @Override
    public SecurityUserInfoMapper getSecurityUserInfoMapper() {
        return securityUserInfoMapper;
    }


    @Override
    public SecurityUserInfo getByEntity(SecurityUserInfo securityUserInfo) {
        return securityUserInfoMapper.getByEntity(securityUserInfo);
    }

    @Override
    public List<SecurityUserInfo> listByEntity(SecurityUserInfo securityUserInfo) {
        return securityUserInfoMapper.listByEntity(securityUserInfo);
    }


    @Override
    public int insert(SecurityUserInfo securityUserInfo) {
        return securityUserInfoMapper.insert(securityUserInfo);
    }

    @Override
    public int insertBatch(List<SecurityUserInfo> list) {
        return securityUserInfoMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityUserInfo securityUserInfo) {
        return securityUserInfoMapper.update(securityUserInfo);
    }

    @Override
    public int updateBatch(List<SecurityUserInfo> list) {
        return securityUserInfoMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer userId) {
        return securityUserInfoMapper.deleteById(userId);
    }

    @Override
    public int deleteByEntity(SecurityUserInfo securityUserInfo) {
        return securityUserInfoMapper.deleteByEntity(securityUserInfo);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityUserInfoMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityUserInfoMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityUserInfo securityUserInfo) {
        return securityUserInfoMapper.countByEntity(securityUserInfo);
    }

}

