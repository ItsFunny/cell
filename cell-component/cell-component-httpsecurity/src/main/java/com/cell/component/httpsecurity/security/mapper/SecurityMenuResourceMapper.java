package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityMenuResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityMenuResourceMapper extends BaseMapper<SecurityMenuResource> {

    SecurityMenuResource getById(Integer menuResourceId);

    List<SecurityMenuResource> listByEntity(SecurityMenuResource securityMenuResource);

    SecurityMenuResource getByEntity(SecurityMenuResource securityMenuResource);

    List<SecurityMenuResource> listByIds(List<Integer> list);

    int insert(SecurityMenuResource securityMenuResource);

    int insertBatch(List<SecurityMenuResource> list);

    int update(SecurityMenuResource securityMenuResource);

    int updateByField(@Param("where") SecurityMenuResource where, @Param("set") SecurityMenuResource set);

    int updateBatch(List<SecurityMenuResource> list);

    int deleteById(Integer menuResourceId);

    int deleteByEntity(SecurityMenuResource securityMenuResource);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityMenuResource securityMenuResource);
}

