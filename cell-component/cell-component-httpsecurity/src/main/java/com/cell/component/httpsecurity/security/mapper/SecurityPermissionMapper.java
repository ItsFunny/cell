package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityPermissionMapper extends BaseMapper<SecurityPermission> {

    SecurityPermission getById(Integer permissionId);

    List<SecurityPermission> listByEntity(SecurityPermission securityPermission);

    SecurityPermission getByEntity(SecurityPermission securityPermission);

    List<SecurityPermission> listByIds(List<Integer> list);

    int insert(SecurityPermission securityPermission);

    int insertBatch(List<SecurityPermission> list);

    int update(SecurityPermission securityPermission);

    int updateByField(@Param("where") SecurityPermission where, @Param("set") SecurityPermission set);

    int updateBatch(List<SecurityPermission> list);

    int deleteById(Integer permissionId);

    int deleteByEntity(SecurityPermission securityPermission);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityPermission securityPermission);
}

