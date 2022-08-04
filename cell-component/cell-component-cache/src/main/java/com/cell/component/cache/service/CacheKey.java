package com.cell.component.cache.service;

public interface CacheKey extends  Cache
{
    String cacheKey();

    String serialize(Object o);

    Object from(String str);
}
