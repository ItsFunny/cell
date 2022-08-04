package com.cell.component.cache.service.impl;

import com.cell.base.common.context.InitCTX;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.core.timewheel.ITimeWheelTaskExecutor;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.INodeContext;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

public class DefaultMemoryCacheServiceImpl<V> implements ICacheService<String, V>
{

    private Map<String, ValueWrapper<V>> cache = new HashMap<>();
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
    public V get(String k)
    {
        try
        {
            this.rwLock.readLock().lock();
            ValueWrapper<V> ValueWrapper = this.cache.get(k);
            if (ValueWrapper != null)
            {
                return ValueWrapper.getV();
            }
            return null;
        } finally
        {
            this.rwLock.readLock().unlock();
        }
    }

    @Override
    public void set(String s, V s2, int delaySeconds)
    {
        ValueWrapper<V> valueWrapper = new ValueWrapper<>();
        valueWrapper.setV(s2);
        this.rwLock.writeLock().lock();
        try
        {
            this.cache.put(s, valueWrapper);
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
    public void set(String s, V s2)
    {
        ValueWrapper<V> valueWrapper = new ValueWrapper<>();
        valueWrapper.setV(s2);

        this.rwLock.writeLock().lock();
        try
        {
            this.cache.put(s, valueWrapper);
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }

    @Override
    public void setIfAbsent(String s, V v)
    {
        this.rwLock.writeLock().lock();
        try
        {
            if (this.cache.containsKey(s))
            {
                return;
            }
            ValueWrapper<V> valueWrapper = new ValueWrapper<>();
            valueWrapper.setV(v);
            this.cache.put(s, valueWrapper);
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }

    @Override
    public void setIfAbsent(String s, V v, int delaySeconds)
    {
        this.rwLock.writeLock().lock();
        try
        {
            if (this.cache.containsKey(s))
            {
                return;
            }
            ValueWrapper<V> valueWrapper = new ValueWrapper<>();
            valueWrapper.setV(v);
            this.cache.put(s, valueWrapper);
            this.executor.addTask((vv) ->
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
    public V delete(String s)
    {
        this.rwLock.writeLock().lock();
        try
        {
            ValueWrapper<V> wrapper = this.cache.remove(s);
            if (wrapper != null)
            {
                return wrapper.getV();
            }
            return null;
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, V> deleteBatchWithReturn(List<String> strings)
    {
        Map<String, V> ret = new HashMap<>();
        this.rwLock.writeLock().lock();
        try
        {
            for (String string : strings)
            {
                ValueWrapper<V> v = this.cache.remove(string);
                if (v != null)
                {
                    ret.put(string, v.getV());
                }
            }
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
        return ret;
    }

    @Override
    public void deleteBatch(Collection<String> strings)
    {
        if (CollectionUtils.isEmpty(strings))
        {
            return;
        }
        this.rwLock.writeLock().lock();
        try
        {
            for (String string : strings)
            {
                this.cache.remove(string);
            }
        } finally
        {
            this.rwLock.writeLock().unlock();
        }
    }

    @Override
    public void deleteByPattern(String pattern, Integer limit)
    {
        this.deleteBatch(this.keysByPattern(pattern, limit));
    }

    @Override
    public boolean contains(String k)
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

    @Override
    public Set<String> keysByPattern(String pattern, Integer limit)
    {
        Set<String> ret = new HashSet<>();
        this.rwLock.readLock().lock();
        try
        {
            for (String k : cache.keySet())
            {
                if (Pattern.matches(pattern, k))
                {
                    ret.add(k);
                    if (ret.size() >= limit)
                    {
                        break;
                    }
                }
            }
            return ret;
        } finally
        {
            this.rwLock.readLock().unlock();
        }
    }
}
