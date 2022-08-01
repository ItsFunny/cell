package com.cell.base.core.services;

import com.cell.base.common.decorators.IDecorator;

/**
 * @author Charlie
 * @When
 * @Description 只用于普通的实体类
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 12:01
 */
public interface ITO<T>
{
    T to(IDecorator<T> ...t);
}
