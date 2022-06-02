package com.cell.base.core.cache;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.base.core.timewheel.ITimeWheelTaskExecutor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultMemCache<T> extends AbstractInitOnce implements ICache<T>
{
    private ReentrantReadWriteLock lock;
    private Map<String, T> cache = new HashMap<>();
    private ITimeWheelTaskExecutor timeWheelTaskExecutor;

    public DefaultMemCache(ITimeWheelTaskExecutor executor)
    {
        this.lock = new ReentrantReadWriteLock();
        this.timeWheelTaskExecutor=executor;
    }

    @Override
    public void addCacheObj(String key, T t, Long expireTime)
    {
        try
        {
            this.lock.writeLock().lock();
            this.cache.put(key, t);
            this.timeWheelTaskExecutor.addTask((timeout) ->
            {
                try
                {
                    this.lock.writeLock().lock();
                    this.cache.remove(key);
                } finally
                {
                    this.lock.writeLock().unlock();
                }
                return Mono.empty();
            }, TimeUnit.SECONDS, expireTime / 1000);

        } finally
        {
            this.lock.writeLock().unlock();
        }

    }

    @Override
    public T getCacheObj(String key, Class<T> clazz)
    {
        try
        {
            this.lock.readLock().lock();
            return this.cache.get(key);
        } finally
        {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void addCacheObjList(String key, List<T> list, Long expireTime)
    {

    }

    @Override
    public List<T> getCacheObjList(String key, Class<T> clazz)
    {
        return null;
    }

    @Override
    public void addCache(String key, String value, Long expireTime)
    {

    }

    @Override
    public String getCacheValue(String key)
    {
        return null;
    }

    @Override
    public String setIfAbsent(String key, String value, Long expireTime)
    {
        return null;
    }

    @Override
    public void clearOpsValue(String key)
    {

    }

    @Override
    public void setOptions(String key, String value, Long expireTime)
    {

    }

    @Override
    public String getIfPresent(String key)
    {
        return null;
    }

    @Override
    public void removeValueOptionsValue(String key)
    {

    }

    @Override
    public void addCache(CacheEntity cacheEntity)
    {

    }

    @Override
    public void addCache(List<CacheEntity> cacheEntityList)
    {

    }

    @Override
    public CacheEntity getCache(String key)
    {
        return null;
    }

    @Override
    public List<CacheEntity> getCache(List<String> keys)
    {
        return null;
    }

    @Override
    public void removeCache(String key)
    {

    }

    @Override
    public void removeCache(List<String> keys)
    {

    }

    @Override
    public void addCounter(String key, Long counter, Long expireTime)
    {

    }

    @Override
    public Long increAndGet(String key, Long delta, Long expireTime)
    {
        return null;
    }

    @Override
    public Long getCounter(String key)
    {
        return null;
    }

    @Override
    public void leftPush(String key, String value)
    {

    }

    @Override
    public void rightPush(String key, String value)
    {

    }

    @Override
    public void rightPushAll(String key, List<String> values)
    {

    }

    @Override
    public String leftPop(String key)
    {
        return null;
    }

    @Override
    public String rightPop(String key)
    {
        return null;
    }

    @Override
    public List<String> range(String key, long start, long end)
    {
        return null;
    }

    @Override
    public Long listSize(String key)
    {
        return null;
    }

    @Override
    public void trim(String key, long start, long end)
    {

    }

    @Override
    public void set(byte[] key, byte[] value)
    {

    }

    @Override
    public byte[] get(byte[] key)
    {
        return new byte[0];
    }

    @Override
    public boolean tryLock(String key, long expireTime, Long maxWait)
    {
        return false;
    }

    @Override
    public boolean tryLock(String key, String value, long expireTime, Long maxWait)
    {
        return false;
    }

    @Override
    public void releaseLock(String key)
    {

    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.timeWheelTaskExecutor.initOnce(ctx);
    }
}
