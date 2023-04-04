package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityGroupMapper extends BaseMapper<SecurityGroup> {

    SecurityGroup getById(Integer groupId);

    List<SecurityGroup> listByEntity(SecurityGroup securityGroup);

    SecurityGroup getByEntity(SecurityGroup securityGroup);

    List<SecurityGroup> listByIds(List<Integer> list);

    int insert(SecurityGroup securityGroup);

    int insertBatch(List<SecurityGroup> list);

    int update(SecurityGroup securityGroup);

    int updateByField(@Param("where") SecurityGroup where, @Param("set") SecurityGroup set);

    int updateBatch(List<SecurityGroup> list);

    int deleteById(Integer groupId);

    int deleteByEntity(SecurityGroup securityGroup);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityGroup securityGroup);
}

