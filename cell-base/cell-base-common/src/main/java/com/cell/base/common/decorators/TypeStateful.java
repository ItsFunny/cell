package com.cell.base.common.decorators;


import com.cell.base.common.enums.BeeEnums;
import com.cell.base.common.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description 有状态 & 有归属的成员
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:04
 */
// FIXME 提供一个default 的对象
public interface TypeStateful<T> extends Stateful<Long>, TypeFul<T>
{
    BeeEnums getBee();
}
