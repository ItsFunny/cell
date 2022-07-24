package com.cell.component.cache.service;

import com.cell.node.core.context.INodeContext;

public interface ICacheService<K, V>
{
    void start(INodeContext context);

    V get(K k);

    void set(K k, V v, int delaySeconds);

    void set(K k, V v);

    V delete(K k);

    boolean contains(K k);
}
