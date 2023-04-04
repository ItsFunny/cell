package com.cell.component.httpsecurity.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.httpsecurity.security.mapper.SecurityUserGroupMapper;
import com.cell.component.httpsecurity.security.entity.SecurityUserGroup;
import com.cell.component.httpsecurity.security.service.SecurityUserGroupService;
import org.springframework.stereotype.Service;
import com.cell.component.httpsecurity.security.mapper.SecurityUserGroupMapper;

import javax.annotation.Resource;
import java.util.List;


@Service("securityUserGroupService")
public class SecurityUserGroupServiceImpl extends ServiceImpl<SecurityUserGroupMapper, SecurityUserGroup> implements SecurityUserGroupService {

    @Resource(type = SecurityUserGroupMapper.class)
    private SecurityUserGroupMapper securityUserGroupMapper;

    @Override
    public SecurityUserGroupMapper getSecurityUserGroupMapper() {
        return securityUserGroupMapper;
    }


    @Override
    public SecurityUserGroup getByEntity(SecurityUserGroup securityUserGroup) {
        return securityUserGroupMapper.getByEntity(securityUserGroup);
    }

    @Override
    public List<SecurityUserGroup> listByEntity(SecurityUserGroup securityUserGroup) {
        return securityUserGroupMapper.listByEntity(securityUserGroup);
    }


    @Override
    public int insert(SecurityUserGroup securityUserGroup) {
        return securityUserGroupMapper.insert(securityUserGroup);
    }

    @Override
    public int insertBatch(List<SecurityUserGroup> list) {
        return securityUserGroupMapper.insertBatch(list);
    }

    @Override
    public int update(SecurityUserGroup securityUserGroup) {
        return securityUserGroupMapper.update(securityUserGroup);
    }

    @Override
    public int updateBatch(List<SecurityUserGroup> list) {
        return securityUserGroupMapper.updateBatch(list);
    }

    @Override
    public int deleteById(Integer id) {
        return securityUserGroupMapper.deleteById(id);
    }

    @Override
    public int deleteByEntity(SecurityUserGroup securityUserGroup) {
        return securityUserGroupMapper.deleteByEntity(securityUserGroup);
    }

    @Override
    public int deleteByIds(List<Integer> list) {
        return securityUserGroupMapper.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return securityUserGroupMapper.countAll();
    }

    @Override
    public int countByEntity(SecurityUserGroup securityUserGroup) {
        return securityUserGroupMapper.countByEntity(securityUserGroup);
    }

}

