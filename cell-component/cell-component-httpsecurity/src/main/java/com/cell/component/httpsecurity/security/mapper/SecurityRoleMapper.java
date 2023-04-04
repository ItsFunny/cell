package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityRoleMapper extends BaseMapper<SecurityRole> {

    SecurityRole getById(Integer roleId);

    List<SecurityRole> listByEntity(SecurityRole securityRole);

    SecurityRole getByEntity(SecurityRole securityRole);

    List<SecurityRole> listByIds(List<Integer> list);

    int insert(SecurityRole securityRole);

    int insertBatch(List<SecurityRole> list);

    int update(SecurityRole securityRole);

    int updateByField(@Param("where") SecurityRole where, @Param("set") SecurityRole set);

    int updateBatch(List<SecurityRole> list);

    int deleteById(Integer roleId);

    int deleteByEntity(SecurityRole securityRole);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityRole securityRole);
}

