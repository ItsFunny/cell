package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityRolePermissionMapper extends BaseMapper<SecurityRolePermission> {

    SecurityRolePermission getById(Integer id);

    List<SecurityRolePermission> listByEntity(SecurityRolePermission securityRolePermission);

    SecurityRolePermission getByEntity(SecurityRolePermission securityRolePermission);

    List<SecurityRolePermission> listByIds(List<Integer> list);

    int insert(SecurityRolePermission securityRolePermission);

    int insertBatch(List<SecurityRolePermission> list);

    int update(SecurityRolePermission securityRolePermission);

    int updateByField(@Param("where") SecurityRolePermission where, @Param("set") SecurityRolePermission set);

    int updateBatch(List<SecurityRolePermission> list);

    int deleteById(Integer id);

    int deleteByEntity(SecurityRolePermission securityRolePermission);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityRolePermission securityRolePermission);
}

