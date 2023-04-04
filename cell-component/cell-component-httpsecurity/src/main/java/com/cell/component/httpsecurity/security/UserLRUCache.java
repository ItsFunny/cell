package com.cell.component.httpsecurity.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import org.springframework.security.core.Authentication;

import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description 缓存用户信息
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 11:22 上午
 */
@Data
public class UserLRUCache {
    private Cache<String, Authentication> cache;

    public void seal() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .maximumSize(10)
                .build();
    }

    public void setUser(String key, Authentication user) {
        this.cache.put(key, user);
    }


    public Authentication getUser(String user) {
        return this.cache.getIfPresent(user);
    }

    private UserLRUCache() {

    }

    private static class UserLRUCacheFactory {
        private static final UserLRUCache instance = new UserLRUCache();
    }

    public static UserLRUCache getInstance() {
        return UserLRUCacheFactory.instance;
    }

}
