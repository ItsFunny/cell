package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityUserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityUserGroupMapper extends BaseMapper<SecurityUserGroup> {

    SecurityUserGroup getById(Integer id);

    List<SecurityUserGroup> listByEntity(SecurityUserGroup securityUserGroup);

    SecurityUserGroup getByEntity(SecurityUserGroup securityUserGroup);

    List<SecurityUserGroup> listByIds(List<Integer> list);

    int insert(SecurityUserGroup securityUserGroup);

    int insertBatch(List<SecurityUserGroup> list);

    int update(SecurityUserGroup securityUserGroup);

    int updateByField(@Param("where") SecurityUserGroup where, @Param("set") SecurityUserGroup set);

    int updateBatch(List<SecurityUserGroup> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityUserGroup securityUserGroup);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityUserGroup securityUserGroup);
}

