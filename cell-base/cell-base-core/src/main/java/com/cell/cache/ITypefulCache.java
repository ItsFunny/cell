package com.cell.cache;

import com.cell.enums.CacheTypeEnums;
import com.cell.services.TypeFul;
import com.google.common.cache.Cache;

/**
 * @author Charlie
 * @When
 * @Description 表明缓存是通过type 进行区分的
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:47
 */
public interface ITypefulCache<K, V> extends Cache<K, V>, TypeFul<CacheTypeEnums>
{
}
