package com.cell.filters;

import com.cell.enums.TypeEnums;
import com.cell.enums.FilterEnums;

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
