package com.cell.base.core.cache;

import com.cell.base.common.context.IInitOnce;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:45
 */
public interface ICache<T> extends IInitOnce
{
    void addCacheObj(String key, T t, Long expireTime);

    T getCacheObj(String key, Class<T> clazz);

    void addCacheObjList(String key, List<T> list, Long expireTime);

    List<T> getCacheObjList(String key, Class<T> clazz);

    void addCache(String key, String value, Long expireTime);

    String getCacheValue(String key);

    String setIfAbsent(String key, String value, Long expireTime);

    void clearOpsValue(String key);

    void setOptions(String key, String value, Long expireTime);

    String getIfPresent(String key);

    void removeValueOptionsValue(String key);

    void addCache(CacheEntity cacheEntity);

    void addCache(List<CacheEntity> cacheEntityList);

    CacheEntity getCache(String key);

    List<CacheEntity> getCache(List<String> keys);

    void removeCache(String key);

    void removeCache(List<String> keys);


    // ======================计数器相关======================

    void addCounter(String key, Long counter, Long expireTime);

    Long increAndGet(String key, Long delta, Long expireTime);

    Long getCounter(String key);

    // ======================计数器相关======================


    // ======================list相关======================

    void leftPush(String key, String value);

    void rightPush(String key, String value);

    void rightPushAll(String key, List<String> values);

    String leftPop(String key);

    String rightPop(String key);

    List<String> range(String key, long start, long end);

    Long listSize(String key);

    void trim(String key, long start, long end);
    // ======================list相关======================

    // ======================byte[]相关======================
    void set(byte[] key, byte[] value);

    byte[] get(byte[] key);
    // ======================byte[]相关======================

    boolean tryLock(String key, long expireTime, Long maxWait);
// 分布式锁相关

    boolean tryLock(String key, String value, long expireTime, Long maxWait);

    void releaseLock(String key);
}
