package com.cell.component.cache.service.impl;

import com.cell.base.common.context.InitCTX;
import com.cell.base.core.timewheel.ITimeWheelTaskExecutor;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.INodeContext;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultMemoryCacheServiceImpl<K, V> implements ICacheService<K, V>
{
    private Map<K, V> cache = new HashMap<>();
    private ITimeWheelTaskExecutor executor;
    private ReentrantReadWriteLock rwLock;

    public DefaultMemoryCacheServiceImpl(ITimeWheelTaskExecutor executor)
    {
        this.executor = executor;
        this.rwLock = new ReentrantReadWriteLock();
    }

    @Override
    public void start(INodeContext context)
    {
        InitCTX ctx = new InitCTX();
        this.executor.initOnce(ctx);
    }

    @Override
    public V get(K k)
    {
        try
        {
            this.rwLock.readLock().lock();
            return this.cache.get(k);
        } finally
        {
            this.rwLock.readLock().unlock();
        }
    }

    @Override
    public void set(K s, V s2, int delaySeconds)
    {
        this.rwLock.writeLock().lock();
        try
        {
            this.cache.put(s, s2);
            this.executor.addTask((v) ->
            {
                this.delete(s);
                return Mono.empty();
            }, TimeUnit.SECONDS, delaySeconds);
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }


    @Override
    public void set(K s, V s2)
    {
        this.rwLock.writeLock().lock();
        try
        {
            this.cache.put(s, s2);
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }

    @Override
    public V delete(K s)
    {
        this.rwLock.writeLock().lock();
        try
        {
            return this.cache.remove(s);
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }

    @Override
    public boolean contains(K k)
    {
        this.rwLock.readLock().lock();
        try
        {
            return this.cache.containsKey(k);
        } finally
        {
            this.rwLock.readLock().unlock();
        }
    }
}
