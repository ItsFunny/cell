package com.cell.component.cache.service;

import java.util.concurrent.TimeUnit;

public interface CacheKey extends Cache
{
    String cacheKey();

    String serialize(Object o);

    Object from(String str);

    default int expire()
    {
        return 0;
    }

    default TimeUnit expireUnit()
    {
        return TimeUnit.SECONDS;
    }
}
