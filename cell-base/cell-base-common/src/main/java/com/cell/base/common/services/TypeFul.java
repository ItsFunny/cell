package com.cell.base.common.services;

/**
 * @author Charlie
 * @When
 * @Description 表明是有类别的, 通常用于匹配判断(如责任链)
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:28
 */
public interface TypeFul<T>
{
    T getType();
}
