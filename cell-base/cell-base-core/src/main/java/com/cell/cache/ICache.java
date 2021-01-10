package com.cell.cache;

import com.cell.decorators.Stateful;
import com.cell.enums.CacheTypeEnums;
import com.cell.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:45
 */
public interface ICache<K, V>
{
    V get(K k);

    void put(K k, V v);
}
