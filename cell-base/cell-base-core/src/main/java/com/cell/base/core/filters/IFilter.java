package com.cell.base.core.filters;

import com.cell.base.core.enums.FilterEnums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 21:56
 */
public interface IFilter<T>
{
    FilterEnums filter(T t);
}
