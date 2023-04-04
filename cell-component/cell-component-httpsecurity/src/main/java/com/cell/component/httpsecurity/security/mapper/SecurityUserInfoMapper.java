package com.cell.component.httpsecurity.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cell.component.httpsecurity.security.entity.SecurityUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecurityUserInfoMapper extends BaseMapper<SecurityUserInfo> {

    SecurityUserInfo getById(Integer userId);

    List<SecurityUserInfo> listByEntity(SecurityUserInfo securityUserInfo);

    SecurityUserInfo getByEntity(SecurityUserInfo securityUserInfo);

    List<SecurityUserInfo> listByIds(List<Integer> list);

    int insert(SecurityUserInfo securityUserInfo);

    int insertBatch(List<SecurityUserInfo> list);

    int update(SecurityUserInfo securityUserInfo);

    int updateByField(@Param("where") SecurityUserInfo where, @Param("set") SecurityUserInfo set);

    int updateBatch(List<SecurityUserInfo> list);

    int deleteById(Integer userId);

    int deleteByEntity(SecurityUserInfo securityUserInfo);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(SecurityUserInfo securityUserInfo);
}

