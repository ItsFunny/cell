package com.cell.component.cache.service;

import com.cell.node.core.context.INodeContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICacheService<K, V>
{
    void start(INodeContext context);

    V get(K k);

    void set(K k, V v, int delaySeconds);
    void set(K k, V v);
    Boolean setIfAbsent(K k, V v);
    Boolean setIfAbsent(K k, V v, int delaySeconds);


    V delete(K k);
    Map<String,V>  deleteBatchWithReturn(List<K> ks);
    void deleteBatch(Collection<K> ks);

    void deleteByPattern(String pattern,Integer limit);

    boolean contains(K k);


    Set<String>keysByPattern(String pattern,Integer limit);
}
