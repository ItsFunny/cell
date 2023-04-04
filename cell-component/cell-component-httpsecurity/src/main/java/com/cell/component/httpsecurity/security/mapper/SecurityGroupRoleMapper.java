package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityGroupRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityGroupRoleMapper extends BaseMapper<SecurityGroupRole> {

    SecurityGroupRole getById(Integer id);

    List<SecurityGroupRole> listByEntity(SecurityGroupRole securityGroupRole);

    SecurityGroupRole getByEntity(SecurityGroupRole securityGroupRole);

    List<SecurityGroupRole> listByIds(List<Integer> list);

    int insert(SecurityGroupRole securityGroupRole);

    int insertBatch(List<SecurityGroupRole> list);

    int update(SecurityGroupRole securityGroupRole);

    int updateByField(@Param("where") SecurityGroupRole where, @Param("set") SecurityGroupRole set);

    int updateBatch(List<SecurityGroupRole> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityGroupRole securityGroupRole);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityGroupRole securityGroupRole);
}

