package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityUserRoleMapper extends BaseMapper<SecurityUserRole> {

    SecurityUserRole getById(Integer id);

    List<SecurityUserRole> listByEntity(SecurityUserRole securityUserRole);

    SecurityUserRole getByEntity(SecurityUserRole securityUserRole);

    List<SecurityUserRole> listByIds(List<Integer> list);

    int insert(SecurityUserRole securityUserRole);

    int insertBatch(List<SecurityUserRole> list);

    int update(SecurityUserRole securityUserRole);

    int updateByField(@Param("where") SecurityUserRole where, @Param("set") SecurityUserRole set);

    int updateBatch(List<SecurityUserRole> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityUserRole securityUserRole);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityUserRole securityUserRole);
}

