package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityOperationResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityOperationResourceMapper extends BaseMapper<SecurityOperationResource> {

    SecurityOperationResource getById(Integer opId);

    List<SecurityOperationResource> listByEntity(SecurityOperationResource securityOperationResource);

    SecurityOperationResource getByEntity(SecurityOperationResource securityOperationResource);

    List<SecurityOperationResource> listByIds(List<Integer> list);

    int insert(SecurityOperationResource securityOperationResource);

    int insertBatch(List<SecurityOperationResource> list);

    int update(SecurityOperationResource securityOperationResource);

    int updateByField(@Param("where") SecurityOperationResource where, @Param("set") SecurityOperationResource set);

    int updateBatch(List<SecurityOperationResource> list);

    int deleteById(Integer opId);

    int deleteByEntity(SecurityOperationResource securityOperationResource);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityOperationResource securityOperationResource);
}

