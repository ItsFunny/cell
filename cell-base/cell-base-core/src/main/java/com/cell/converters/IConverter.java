package com.cell.converters;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:39
 */
public interface IConverter<F, T>
{
    T convert(F f);
}
