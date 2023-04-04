package com.cell.component.httpsecurity.security.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.component.httpsecurity.security.constants.SecurityConstants;
import com.cell.component.httpsecurity.security.entity.SecurityUserInfo;
import com.cell.component.httpsecurity.security.service.SecurityUserInfoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class DBCache implements InitializingBean {
    @Autowired
    private SecurityUserInfoService securityUserInfoService;
    private ReentrantReadWriteLock userRWLock = new ReentrantReadWriteLock();
    private Set<Integer> blockUserSet = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        QueryWrapper<SecurityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", SecurityConstants.DB_USER_STATUS_DISABLE);
        List<SecurityUserInfo> users = this.securityUserInfoService.list(queryWrapper);
        userRWLock.writeLock().lock();
        try {
            for (SecurityUserInfo user : users) {
                blockUserSet.add(user.getUserId());
            }
        } finally {
            userRWLock.writeLock().unlock();
        }
    }
}
